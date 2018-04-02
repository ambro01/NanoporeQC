package com.nanoporeqc.analyse;

import com.nanoporeqc.fast5.FileConsts;
import com.nanoporeqc.r.service.RService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class AnalyseService {

    RService rService;

    @Autowired
    public AnalyseService(RService rService) {
        this.rService = rService;
    }

    public void runNewAnalyse(String analyseName) throws IOException {
        rService.readFast5FilesFromDir(FileConsts.FAST5FILES_DIR);
        FileUtils.cleanDirectory(new File(FileConsts.FAST5FILES_DIR));
        rService.saveSummaryToFile(analyseName);
    }
}
