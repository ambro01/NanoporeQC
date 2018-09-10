package com.nanoporeqc.r.service;

import com.nanoporeqc.analysis.domain.ClusteringAlgorithm;
import com.nanoporeqc.analysis.domain.Type;
import com.nanoporeqc.analysis.service.StatsService;
import com.nanoporeqc.exceptions.NotSupportedAnalysisTypeException;
import com.nanoporeqc.exceptions.REvaluatingException;
import com.nanoporeqc.file.consts.FileConsts;
import com.nanoporeqc.file.service.FileService;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.enumeration.RDataEnum;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class RService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsService.class);

    private final ObjectFactory<RConnection> rConnection;

    private final FileService fileService;

    private final ReentrantLock lock = new ReentrantLock();

    private final ReentrantLock basicLock = new ReentrantLock();

    @Autowired
    public RService(final ObjectFactory<RConnection> rConnection,
                    final FileService fileService) {
        this.rConnection = rConnection;
        this.fileService = fileService;
    }

    @PostConstruct
    public void init() throws URISyntaxException, RserveException, IOException {
        RConnection rCon = new RConnection();
        LOGGER.info("Copy scripts to local disc");
        copyAllRScriptsToDisc();
        LOGGER.info("Run initial R scripts:");
        for (RScriptEnum rScriptEnum : RScriptEnum.INITIALS_SCRIPTS) {
            rCon.serverEval(String.format("source('%s')", fileService.getRScriptPath(rScriptEnum)));
            LOGGER.info("R: Load script " + rScriptEnum.getFileName());
        }
        rCon.close();
    }

    private REXP eval(String cmd) {
        basicLock.lock();
        try {
            return rConnection.getObject().eval(cmd);
        } catch (RserveException e) {
            throw new REvaluatingException();
        } finally {
            basicLock.unlock();
        }
    }

    public void evaluateRScript(final RScriptEnum rScriptEnum) {
        eval(String.format("source('%s')", fileService.getRScriptPath(rScriptEnum)));
    }

    private List getDataSetFromR(final RDataEnum rDataEnum, final RVariable rVariable) {
        final String notSaved = rDataEnum.isNotSaved() ? "NotSaved" : "";
        final String variableType = rDataEnum.getType() != null ? "results" + rDataEnum.getType() + notSaved + "$" : "";
        final String commandPrefix = variableType + rDataEnum.getValue() + "$";
        LOGGER.info("Get data for variable: " + variableType + rVariable.getName());
        try {
            switch (rVariable.getType()) {
                case LOGICAL:
                    return Arrays.stream(eval(commandPrefix + rVariable.getName()).asIntegers())
                            .boxed()
                            .collect(Collectors.toList());
                case DOUBLE:
                    return Arrays.stream(eval(commandPrefix + rVariable.getName()).asDoubles())
                            .boxed()
                            .map(aDouble -> {
                                        if (rVariable.getPrecision() != null) {
                                            return BigDecimal.valueOf(aDouble)
                                                    .setScale(rVariable.getPrecision(), RoundingMode.HALF_UP)
                                                    .doubleValue();
                                        }
                                        return aDouble;
                                    }
                            )
                            .collect(Collectors.toList());
                case NUMERIC:
                    return Arrays.stream(eval(commandPrefix + rVariable.getName()).asIntegers())
                            .boxed()
                            .collect(Collectors.toList());
                case CHARACTER:
                    return Arrays.asList(eval(commandPrefix + rVariable.getName()).asStrings());
                default:
                    break;
            }
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void loadFilesFromDirToR(final RScriptEnum rScriptEnum) {
        lock.lock();
        try {
            eval("dirPath <- " + "'" + FileConsts.FILES_DIR + "'");
            eval(String.format("source('%s')", fileService.getRScriptPath(rScriptEnum)));
        } finally {
            lock.unlock();
        }
    }

    public void saveSummaryToFile(final Type type, final String summaryPath) {
        final RScriptEnum rScriptEnum = getSaveSummaryScript(type);
        lock.lock();
        try {
            eval("summaryPath <- " + "'" + summaryPath + "'");
            eval(String.format("source('%s')", fileService.getRScriptPath(rScriptEnum)));
        } finally {
            lock.unlock();
        }
    }

    public void loadSummaryFromFile(final RScriptEnum rScriptEnum, final String summaryPath) {
        lock.lock();
        try {
            eval("summaryPath <- " + "'" + summaryPath + "'");
            eval(String.format("source('%s')", fileService.getRScriptPath(rScriptEnum)));
            fileService.cleanDirectory(FileConsts.SUMMARY_DIR);
        } finally {
            lock.unlock();
        }
    }

    public void runClusteringReads(final Type type, final ClusteringAlgorithm clusteringAlgorithm, final Integer clustersNumber) {
        final String savedVariable = "results" + type.name() + "$clusteringData";
        eval("preparedClusteringData <- " + savedVariable);
        eval("clustersNumber <- " + clustersNumber);
        switch (clusteringAlgorithm) {
            case kmeans:
                eval("kmeansClustering <- runKmeansClustering(preparedClusteringData, clustersNumber)");
            case mclust:
                if (clustersNumber > 0) {
                    eval("mclustClustering <- runMclustClustering(preparedClusteringData, clustersNumber)");
                } else {
                    eval("mclustClusteringWithoutGroupNumber <- runMclustClusteringWithoutGroupNumber(preparedClusteringData)");
                }
        }
    }

    public void runD2Detection(final Type type) {
        final String savedVariable = "results" + type.name() + "$clusteringData";
        eval("preparedClusteringData <- " + savedVariable);
        eval("d2Detection <- run2dDetection(preparedClusteringData)");
    }

    public void runOutliersDetection(final Type type, final Double outliersProportion) {
        final String savedVariable = "results" + type.name() + "$clusteringData";
        eval("preparedClusteringData <- " + savedVariable);
        eval("outliersProportion <- " + outliersProportion);
        eval("outliersDetection <- runOutliersDetection(preparedClusteringData, outliersProportion)");
    }

    private void copyAllRScriptsToDisc() {
        fileService.cleanDirectory(FileConsts.SCRIPTS_DIR);
        for (RScriptEnum rScriptEnum : RScriptEnum.values()) {
            fileService.saveRScriptFile(rScriptEnum);
        }
    }

    public void updateRVariableData(final RDataEnum rDataEnum, final RVariable rVariable) {
        lock.lock();
        try {
            rVariable.setRDataSet(getDataSetFromR(rDataEnum, rVariable));
        } finally {
            lock.unlock();
        }
    }

    public RScriptEnum getSummaryFromDirScript(final Type type) {
        switch (type) {
            case Fast5:
                return RScriptEnum.READ_FAST5_SUMMARY_FROM_DIR;
            case FastQ:
                return RScriptEnum.READ_FASTQ_SUMMARY_FROM_DIR;
            default:
                throw new NotSupportedAnalysisTypeException();
        }
    }

    public RScriptEnum getGenerateDataScript(final Type type) {
        switch (type) {
            case Fast5:
                return RScriptEnum.GENERATE_DATA_FAST5;
            case FastQ:
                return RScriptEnum.GENERATE_DATA_FASTQ;
            default:
                throw new NotSupportedAnalysisTypeException();
        }
    }

    public RScriptEnum getReadSummaryScript(final Type type) {
        switch (type) {
            case Fast5:
                return RScriptEnum.READ_SUMMARY_FAST5;
            case FastQ:
                return RScriptEnum.READ_SUMMARY_FASTQ;
            default:
                throw new NotSupportedAnalysisTypeException();
        }
    }

    private RScriptEnum getSaveSummaryScript(final Type type) {
        switch (type) {
            case Fast5:
                return RScriptEnum.SAVE_SUMMARY_FAST5;
            case FastQ:
                return RScriptEnum.SAVE_SUMMARY_FASTQ;
            default:
                throw new NotSupportedAnalysisTypeException();
        }
    }

}
