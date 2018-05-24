package com.nanoporeqc.r.service;

import com.nanoporeqc.analysis.domain.Type;
import com.nanoporeqc.exceptions.NotSupportedAnalysisTypeException;
import com.nanoporeqc.exceptions.REvaluatingException;
import com.nanoporeqc.analysis.service.StatsService;
import com.nanoporeqc.file.consts.FileConsts;
import com.nanoporeqc.file.service.FileService;
import com.nanoporeqc.r.domain.RVariable;
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

    private ReentrantLock lock = new ReentrantLock();

    private ReentrantLock basicLock = new ReentrantLock();

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

    private List getDataSetFromR(final RVariable rVariable) {
        try {
            switch (rVariable.getType()) {
                case LOGICAL:
                    return Arrays.stream(eval(rVariable.getName()).asIntegers())
                            .boxed()
                            .collect(Collectors.toList());
                case DOUBLE:
                    return Arrays.stream(eval(rVariable.getName()).asDoubles())
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
                    return Arrays.stream(eval(rVariable.getName()).asIntegers())
                            .boxed()
                            .collect(Collectors.toList());
                case CHARACTER:
                    return Arrays.asList(eval(rVariable.getName()).asStrings());
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
        fileService.cleanDirectory(FileConsts.FILES_DIR);
    }

    public void loadSummaryAFromSummaryB() {
        final RScriptEnum rScriptEnum = RScriptEnum.READ_FASTQ_SUMMARY_FROM_FAST5_SUMMARY;
        lock.lock();
        try {
            eval("filePath <- " + "'" + FileConsts.FASTQ_FILE_FROM_FAST5 + "'");
            eval("dirPath <- " + "'" + FileConsts.FILES_DIR + "'");
            eval(String.format("source('%s')", fileService.getRScriptPath(rScriptEnum)));
        } finally {
            lock.unlock();
        }
        fileService.cleanDirectory(FileConsts.FILES_DIR);
    }

    public void saveSummaryToFile() {
        lock.lock();
        try {
            eval("summaryName <- " + "'" + FileConsts.SUMMARY_FILE + "'");
            eval(String.format("source('%s')", fileService.getRScriptPath(RScriptEnum.SAVE_SUMMARY)));
        } finally {
            lock.unlock();
        }
    }

    public void loadSummaryFromFile(final String type) {
        final RScriptEnum rScriptEnum;
        switch (Type.valueOf(type)) {
            case Fast5:
                rScriptEnum = RScriptEnum.READ_SUMMARY;
                break;
            case FastQ:
                rScriptEnum = RScriptEnum.READ_QA_SUMMARY;
                break;
            default:
                throw new NotSupportedAnalysisTypeException();
        }
        lock.lock();
        try {
            eval("summaryName <- " + "'" + FileConsts.SUMMARY_FILE + "'");
            eval(String.format("source('%s')", fileService.getRScriptPath(rScriptEnum)));
        } finally {
            lock.unlock();
        }
    }

    private void copyAllRScriptsToDisc() {
        fileService.cleanDirectory(FileConsts.SCRIPTS_DIR);
        for (RScriptEnum rScriptEnum : RScriptEnum.values()) {
            fileService.saveRScriptFile(rScriptEnum);
        }
    }

    public void updateRVariableData(RVariable rVariable) {
        lock.lock();
        try {
            rVariable.setRDataSet(getDataSetFromR(rVariable));
        } finally {
            lock.unlock();
        }
    }
}
