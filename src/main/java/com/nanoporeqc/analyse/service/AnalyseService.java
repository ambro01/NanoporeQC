package com.nanoporeqc.analyse.service;

import com.nanoporeqc.analyse.domain.Analyse;
import com.nanoporeqc.fast5.consts.FileConsts;
import com.nanoporeqc.r.service.RService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AnalyseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseService.class);

    RService rService;

    @Autowired
    public AnalyseService(RService rService) {
        this.rService = rService;
    }

    public void runNewAnalyse(String analyseName) throws IOException {
        LOGGER.info("Analyse: Running new analyse");
        rService.readFast5FilesFromDir(FileConsts.FAST5FILES_DIR);
        FileUtils.cleanDirectory(new File(FileConsts.FAST5FILES_DIR));
        LOGGER.info("Analyse: Saving summary file");
        rService.saveSummaryToFile(analyseName);
        saveNewAnalyse(analyseName);
    }

    private void saveNewAnalyse(String analyseName) {
        Analyse analyse = new Analyse();
        analyse.setName(analyseName);
        analyse.setRunTime(LocalDateTime.now());
        LOGGER.info("Analyse: Analyse " + analyseName + " was saved");
    }


}
