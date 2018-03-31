package com.nanoporeqc.fast5;

import com.nanoporeqc.r.service.RChartService;
import com.nanoporeqc.r.service.RService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RChartService.class);

    static final String DEST_DIR = "/tmp/fast5files/";

    private RService rService;

    @Autowired
    public FileService(RService rService) {
        this.rService = rService;
    }

    public void saveFiles(MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            String filePath = DEST_DIR + file.getOriginalFilename();

            LOGGER.info("Saving file " + filePath);

            File destination = new File(filePath);
            if(destination.createNewFile()) {
                LOGGER.info("Empty file was created");
                file.transferTo(destination);
                LOGGER.info("File content was saved");
            }
        }
        rService.readFast5FilesFromDir(DEST_DIR);
        FileUtils.cleanDirectory(new File(DEST_DIR));
    }
}
