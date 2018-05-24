package com.nanoporeqc.file.service;

import com.nanoporeqc.Config;
import com.nanoporeqc.exceptions.AnalysisSummaryCannotBeSavedException;
import com.nanoporeqc.exceptions.RScriptFileCannotBeSavedException;
import com.nanoporeqc.exceptions.UploadedFilesCannotBeSavedException;
import com.nanoporeqc.file.consts.FileConsts;
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

    public void saveFiles(final MultipartFile[] files) {
        cleanDirectory(FileConsts.FILES_DIR);
        LOGGER.info("Saving files from user");
        for (MultipartFile file : files) {
            final String filePath = FileConsts.FILES_DIR + file.getOriginalFilename();
            final File destination = new File(filePath);
            try {
                if (destination.createNewFile()) {
                    file.transferTo(destination);
                }
            } catch (IOException e) {
                throw new UploadedFilesCannotBeSavedException();
            }
        }
    }

    public void saveRScriptFile(final RScriptEnum rScriptEnum) {
        final URL resource = Config.class.getResource(FileConsts.SCRIPTS_RESOURCE + rScriptEnum.getFileName());
        final File source = new File(resource.getFile());
        final String filePath = FileConsts.SCRIPTS_DIR + rScriptEnum.getFileName();
        final File destination = new File(filePath);
        copySourceToDestination(source, destination);
    }


    public void saveSummaryToFile(final Blob summary) {
        try {
            cleanDirectory(FileConsts.SUMMARY_DIR);
            final InputStream inputStream = summary.getBinaryStream();
            final Path path = Paths.get(FileConsts.SUMMARY_FILE);
            final OutputStream outputStream = new FileOutputStream(path.toFile());
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (SQLException | IOException e) {
            throw new AnalysisSummaryCannotBeSavedException();
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

    public void cleanDirectory(final String dirPath) {
        try {
            FileUtils.cleanDirectory(new File(dirPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copySourceToDestination(final File source, final File destination) {
        if (destination.exists()) {
            return;
        }
        try {
            if (destination.createNewFile()) {
                FileUtils.copyFile(source, destination);
            }
        } catch (IOException e) {
            throw new RScriptFileCannotBeSavedException();
        }
    }
}
