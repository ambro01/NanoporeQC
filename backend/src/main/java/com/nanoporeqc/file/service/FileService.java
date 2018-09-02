package com.nanoporeqc.file.service;

import com.nanoporeqc.Config;
import com.nanoporeqc.exceptions.AnalysisSummaryCannotBeSavedException;
import com.nanoporeqc.exceptions.RScriptFileCannotBeSavedException;
import com.nanoporeqc.exceptions.ReportCannotBeSavedException;
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
        final InputStream in = Config.class.getClassLoader().getResourceAsStream(FileConsts.SCRIPTS_RESOURCE + rScriptEnum.getFileName());
        final String filePath = FileConsts.SCRIPTS_DIR + rScriptEnum.getFileName();
        final File destination = new File(filePath);
        copySourceStreamToDestination(in, destination);
    }


    public void saveSummaryToFile(final Blob summary, final String summaryPath) {
        try {
            cleanDirectory(FileConsts.SUMMARY_DIR);
            final InputStream inputStream = summary.getBinaryStream();
            final Path path = Paths.get(summaryPath);
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

    public byte[] getSummaryContent(final String filePath) {
        final Path path = Paths.get(filePath);
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

    public void createNewDir(final String dirPath) {
        final File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdir();
        } else {
            cleanDirectory(dirPath);
        }
    }

    public File createNewFile(final String filePath) {
        final File file = new File(filePath);
        if (file.exists()) {
            return file;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void saveReport(final Blob report, final String filePath) {
        try {
            final Path path = Paths.get(filePath);
            final InputStream inputStream = report.getBinaryStream();
            final OutputStream outputStream = new FileOutputStream(path.toFile());
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (SQLException | IOException e) {
            throw new ReportCannotBeSavedException();
        }
    }

    private void copySourceStreamToDestination(final InputStream inputStream, final File destination) {
        if (destination.exists()) {
            return;
        }
        try {
            if (destination.createNewFile()) {
                final OutputStream outputStream = new FileOutputStream(destination);
                try {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                } finally {
                    inputStream.close();
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            throw new RScriptFileCannotBeSavedException();
        }
    }
}
