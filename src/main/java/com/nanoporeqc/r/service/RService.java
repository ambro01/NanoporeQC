package com.nanoporeqc.r.service;

import com.nanoporeqc.fast5.consts.FileConsts;
import com.nanoporeqc.r.config.RConfiguration;
import com.nanoporeqc.r.domain.RFast5Resource;
import com.nanoporeqc.r.domain.RVariable;
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
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RChartService.class);

    private RConnection rConnection;

    @Autowired
    public RService(RConnection rConnection) {
        this.rConnection = rConnection;
    }

    @PostConstruct
    public void init() throws URISyntaxException, RserveException, IOException {
        LOGGER.info("Run initial R scripts:");
        LOGGER.info("R: Load libraries");
        URL resource = RConfiguration.class.getResource("/r_scripts/loadLibraries.R");
        rConnection.eval(String.format("source('%s')", Paths.get(resource.toURI()).toFile()));
        LOGGER.info("R: Load common functions");
        resource = RConfiguration.class.getResource("/r_scripts/common.R");
        rConnection.eval(String.format("source('%s')", Paths.get(resource.toURI()).toFile()));
        LOGGER.info("R: Load all functions");
        resource = RConfiguration.class.getResource("/r_scripts/allFunctions.R");
        rConnection.eval(String.format("source('%s')", Paths.get(resource.toURI()).toFile()));
    }

    public void evaluateRScript(URL scriptUrl) {
        try {
            rConnection.eval(String.format("source('%s')", Paths.get(scriptUrl.toURI()).toFile()));
        } catch (RserveException | URISyntaxException e) {
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
        URL resource = RConfiguration.class.getResource("/r_scripts/readFast5SummaryFromDir.R");

        try {
            rConnection.eval("dirPath <- " + "'" + filesDir + "'");
            rConnection.eval(String.format("source('%s')", Paths.get(resource.toURI()).toFile()));
        } catch (RserveException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void saveSummaryToFile(String summaryName) {
        URL resource = RConfiguration.class.getResource("/r_scripts/saveSummary.R");

        try {
            rConnection.eval("summaryName <- " + "'" + FileConsts.SUMMARY_DIR + summaryName + "'");
            rConnection.eval("summaryType <- " + "'" + FileConsts.SUMMARY_TYPE + "'");
            rConnection.eval(String.format("source('%s')", Paths.get(resource.toURI()).toFile()));
        } catch (RserveException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void setUp() {
        RFast5Resource rFast5Resource = new RFast5Resource();
        RFast5ResourceService rFast5ResourceService = new RFast5ResourceService();

        List<URL> urlList = new ArrayList<>();
        urlList.add(this.getClass().getResource("/test/r_test/1.fast5"));
        urlList.add(this.getClass().getResource("/test/r_test/2.fast5"));
        urlList.add(this.getClass().getResource("/test/r_test/3.fast5"));
        urlList.add(this.getClass().getResource("/test/r_test/4.fast5"));
        urlList.add(this.getClass().getResource("/test/r_test/5.fast5"));

        rFast5Resource.setFilesUrls(urlList);

        String filesPath = rFast5ResourceService.getFilesPathsAsString(rFast5Resource);

        try {
            rConnection.eval("files <- " + filesPath);
        } catch (RserveException e) {
            e.printStackTrace();
        }


    }
}
