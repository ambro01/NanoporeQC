package com.nanoporeqc.fast5.service;

import com.nanoporeqc.fast5.consts.FileConsts;
import com.nanoporeqc.r.service.RChartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public void saveFiles(MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            String filePath = FileConsts.FAST5FILES_DIR + file.getOriginalFilename();

            LOGGER.info("Saving file " + filePath);

            File destination = new File(filePath);
            if(destination.createNewFile()) {
                LOGGER.info("Empty file was created");
                file.transferTo(destination);
                LOGGER.info("File content was saved");
            }
        }
    }
}
