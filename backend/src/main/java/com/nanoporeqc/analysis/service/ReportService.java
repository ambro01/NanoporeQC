package com.nanoporeqc.analysis.service;

import com.nanoporeqc.analysis.domain.Analysis;
import com.nanoporeqc.analysis.domain.Type;
import com.nanoporeqc.exceptions.CsvDataCannotBeExportedException;
import com.nanoporeqc.exceptions.NotSupportedAnalysisTypeException;
import com.nanoporeqc.exceptions.ReportCannotBeSavedException;
import com.nanoporeqc.file.consts.FileConsts;
import com.nanoporeqc.file.service.FileService;
import com.nanoporeqc.user.service.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private static final String FAST_QC_TOOL = "fastqc";
    private final static String HTML_TYPE = "text/html";
    private final static String HTML = ".html";

    private final FileService fileService;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ReportService(final FileService fileService,
                         final ApplicationUserService applicationUserService) {
        this.fileService = fileService;
        this.applicationUserService = applicationUserService;
    }

    public void saveLocallyFastQCHtmlReport() {
        final String dirToSave = findHtmlReportDir(FileConsts.FASTQ_REPORT_DIR);
        fileService.createNewDir(dirToSave);
        generateFastQCHtmlReport(dirToSave);
    }

    public String getHtmlReportPath(final String type) {
        final String prefixDir = getReportPrefixDir(type);
        final File reportDir = new File(prefixDir);

        if (reportDir.listFiles() == null) {
            throw new ReportCannotBeSavedException();
        }

        return Arrays.stream(
                Optional.ofNullable(reportDir.listFiles())
                        .orElseThrow(ReportCannotBeSavedException::new))
                .filter(file -> file.getName().endsWith(HTML))
                .findFirst()
                .map(File::getAbsolutePath)
                .orElseThrow(ReportCannotBeSavedException::new);
    }

    public void downloadReport(final Analysis analysis, final HttpServletResponse response) {
        setHtmlAndDownloadHeader(analysis.getName(), response);
        try {
            final OutputStream outputStream = response.getOutputStream();
            final Blob report = analysis.getHtmlReport();
            outputStream.write(report.getBytes(1, (int) report.length()));
            outputStream.flush();
            outputStream.close();
        } catch (IOException | SQLException e) {
            throw new CsvDataCannotBeExportedException();
        }
    }

    public void downloadCurrentReport(final String type, final HttpServletResponse response) {
        setHtmlAndDownloadHeader(type, response);
        try {
            final OutputStream outputStream = response.getOutputStream();
            outputStream.write(fileService.getSummaryContent(getHtmlReportPath(type)));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new CsvDataCannotBeExportedException();
        }
    }

    private void setHtmlAndDownloadHeader(final String name, final HttpServletResponse response) {
        final String fileName = name + HTML;
        response.setContentType(HTML_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("FileName", fileName);
    }

    private String getReportPrefixDir(final String type) {
        switch (Type.valueOf(type)) {
            case Fast5:
                return FileConsts.FAST5_REPORT_DIR + applicationUserService.getCurrentUser().getId();
            case FastQ:
                return FileConsts.FASTQ_REPORT_DIR + applicationUserService.getCurrentUser().getId();
            default:
                throw new NotSupportedAnalysisTypeException();
        }
    }

    private String findHtmlReportDir(final String prefixDir) {
        final Long userId = applicationUserService.getCurrentUser().getId();
        return FileConsts.FASTQ_REPORT_DIR + userId + "/";
    }


    private void generateFastQCHtmlReport(final String dirToSave) {
        try {
            final List<String> filesPaths = Files.list(Paths.get(FileConsts.FILES_DIR))
                    .map(Path::toAbsolutePath)
                    .map(Object::toString)
                    .collect(Collectors.toList());
            final Process process = Runtime.getRuntime().exec(FAST_QC_TOOL + getConnectedFilesPaths(filesPaths) + " --outdir=" + dirToSave);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getConnectedFilesPaths(final List<String> filesPaths) {
        final StringBuilder sb = new StringBuilder();
        for (String path : filesPaths) {
            sb.append(" ").append(path);
        }
        return sb.toString();
    }

}
