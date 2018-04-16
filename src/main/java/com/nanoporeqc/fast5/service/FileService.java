package com.nanoporeqc.fast5.service;

import com.nanoporeqc.fast5.consts.FileConsts;
import com.nanoporeqc.r.config.RConfiguration;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public void saveFiles(final MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            final String filePath = FileConsts.FAST5FILES_DIR + file.getOriginalFilename();

            LOGGER.info("Saving file " + filePath);

            File destination = new File(filePath);
            if (destination.createNewFile()) {
                LOGGER.info("Empty file was created");
                file.transferTo(destination);
                LOGGER.info("File content was saved");
            }
        }
    }

    public void saveRScriptFile(final RScriptEnum rScriptEnum) {
        final URL resource = RConfiguration.class.getResource(FileConsts.SCRIPTS_RESOURCE + rScriptEnum.getFileName());
        final File source = new File(resource.getFile());

        final String filePath = FileConsts.SCRIPTS_DIR + rScriptEnum.getFileName();
        File destination = new File(filePath);

        try {
            if (destination.createNewFile()) {
                FileUtils.copyFile(source, destination);
            }
        } catch (IOException e) {
            LOGGER.info("Is not a bug");
        }
    }

    public String getRScriptPath(final RScriptEnum rScriptEnum) {
        return FileConsts.SCRIPTS_DIR + rScriptEnum.getFileName();
    }

    public byte[] getSummaryContent() {
        final Path path = Paths.get(FileConsts.SUMMARY_FILE);
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void cleanDirectory(String dirPath) {
        try {
            FileUtils.cleanDirectory(new File(dirPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
