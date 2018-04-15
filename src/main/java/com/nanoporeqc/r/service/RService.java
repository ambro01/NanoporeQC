package com.nanoporeqc.r.service;

import com.nanoporeqc.fast5.consts.FileConsts;
import com.nanoporeqc.fast5.service.FileService;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RChartService.class);

    private final RConnection rConnection;

    private final FileService fileService;

    @Autowired
    public RService(final RConnection rConnection, final FileService fileService) {
        this.rConnection = rConnection;
        this.fileService = fileService;
    }

    @PostConstruct
    public void init() throws URISyntaxException, RserveException, IOException {
        LOGGER.info("Copy scripts to local disc");
        copyAllRScriptsToDisc();
        LOGGER.info("Run initial R scripts:");
        for (RScriptEnum rScriptEnum : RScriptEnum.INITIALS_SCRIPTS) {
            rConnection.eval(String.format("source('%s')", fileService.getRScriptPath(rScriptEnum)));
            LOGGER.info("R: Load script " + rScriptEnum.getFileName());
        }
    }

    public void evaluateRScript(final RScriptEnum rScriptEnum) {
        try {
            rConnection.eval(String.format("source('%s')", fileService.getRScriptPath(rScriptEnum)));
        } catch (RserveException e) {
            e.printStackTrace();
        }
    }

    public List getDataSetFromR(RVariable rVariable) {
        try {
            switch (rVariable.getType()) {
                case LOGICAL:
                    return Arrays.stream(rConnection.eval(rVariable.getName()).asIntegers()).boxed().collect(Collectors.toList());
                case DOUBLE:
                    return Arrays.stream(rConnection.eval(rVariable.getName()).asDoubles()).boxed().collect(Collectors.toList());
                case NUMERIC:
                    return Arrays.stream(rConnection.eval(rVariable.getName()).asIntegers()).boxed().collect(Collectors.toList());
                case CHARACTER:
                    return Arrays.asList(rConnection.eval(rVariable.getName()).asStrings());
                default:
                    break;
            }
        } catch (REXPMismatchException | RserveException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void readFast5FilesFromDir(String filesDir) {
        try {
            rConnection.eval("dirPath <- " + "'" + filesDir + "'");
            rConnection.eval(String.format("source('%s')", fileService.getRScriptPath(RScriptEnum.READ_FAST5_SUMMARY_FROM_DIR)));
        } catch (RserveException e) {
            e.printStackTrace();
        }
    }

    public void saveSummaryToFile() {
        try {
            rConnection.eval("summaryName <- " + "'" + FileConsts.SUMMARY_FILE + "'");
            rConnection.eval(String.format("source('%s')", fileService.getRScriptPath(RScriptEnum.SAVE_SUMMARY)));
        } catch (RserveException e) {
            e.printStackTrace();
        }
    }

    private void copyAllRScriptsToDisc() {
        for (RScriptEnum rScriptEnum : RScriptEnum.values()) {
            fileService.saveRScriptFile(rScriptEnum);
        }
    }
}
