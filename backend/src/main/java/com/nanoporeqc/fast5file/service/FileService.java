package com.nanoporeqc.fast5file.service;

import com.nanoporeqc.Config;
import com.nanoporeqc.fast5file.consts.FileConsts;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public void saveFiles(final MultipartFile[] files) throws IOException {
        cleanDirectory(FileConsts.FILES_DIR);
        LOGGER.info("Saving files from user");
        for (MultipartFile file : files) {
            final String filePath = FileConsts.FILES_DIR + file.getOriginalFilename();

            File destination = new File(filePath);
            if (destination.createNewFile()) {
                file.transferTo(destination);
            }
        }
    }

    public void saveRScriptFile(final RScriptEnum rScriptEnum) {
        final URL resource = Config.class.getResource(FileConsts.SCRIPTS_RESOURCE + rScriptEnum.getFileName());
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

    public void saveSummaryToFile(final Blob summary) throws SQLException, IOException {
        InputStream inputStream = summary.getBinaryStream();
        final Path path = Paths.get(FileConsts.SUMMARY_FILE);
        final File destination = path.toFile();
        OutputStream outputStream = new FileOutputStream(destination);

        IOUtils.copy(inputStream, outputStream);

        try {
            destination.createNewFile();
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
