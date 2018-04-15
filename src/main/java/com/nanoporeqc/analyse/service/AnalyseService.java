package com.nanoporeqc.analyse.service;

import com.nanoporeqc.analyse.domain.Analyse;
import com.nanoporeqc.analyse.repository.AnalyseDao;
import com.nanoporeqc.fast5.consts.FileConsts;
import com.nanoporeqc.fast5.service.FileService;
import com.nanoporeqc.r.service.RService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class AnalyseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseService.class);

    private RService rService;

    private FileService fileService;

    private AnalyseDao analyseDao;

    @Autowired
    public AnalyseService(RService rService, FileService fileService, AnalyseDao analyseDao) {
        this.rService = rService;
        this.fileService = fileService;
        this.analyseDao = analyseDao;
    }

    public void runNewAnalyse(String analyseName) throws IOException {
        LOGGER.info("Analyse: Running new analyse");
        rService.readFast5FilesFromDir(FileConsts.FAST5FILES_DIR);
        FileUtils.cleanDirectory(new File(FileConsts.FAST5FILES_DIR));
        LOGGER.info("Analyse: Saving summary file");
        rService.saveSummaryToFile();
        saveNewAnalyse(analyseName);
    }

    @Transactional
    public void saveNewAnalyse(String analyseName) {
        Analyse analyse = new Analyse();
        analyse.setName(analyseName);
        analyse.setRunTime(LocalDateTime.now());
        try {
            analyse.setContent(new SerialBlob(fileService.getSummaryContent()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        analyseDao.save(analyse);
        LOGGER.info("Analyse: Analyse " + analyseName + " was saved");
    }
}
