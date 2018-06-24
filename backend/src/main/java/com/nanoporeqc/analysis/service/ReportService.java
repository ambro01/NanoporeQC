package com.nanoporeqc.analysis.service;

import com.nanoporeqc.analysis.domain.Analysis;
import com.nanoporeqc.analysis.domain.Type;
import com.nanoporeqc.analysis.repository.AnalysisRepository;
import com.nanoporeqc.exceptions.AnalysisNotFoundException;
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
    private static final String FAST_QC_TOOL_CMD = "fastqc";
    private final static String HTML_TYPE = "text/html";
    private final static String HTML = ".html";

    private final FileService fileService;
    private final ApplicationUserService applicationUserService;
    private final AnalysisRepository analysisRepository;

    @Autowired
    public ReportService(final FileService fileService,
                         final ApplicationUserService applicationUserService,
                         final AnalysisRepository analysisRepository) {
        this.fileService = fileService;
        this.applicationUserService = applicationUserService;
        this.analysisRepository = analysisRepository;
    }

    public void saveLocallyFastQCHtmlReport(final String type) {
        final String dirToSave = findHtmlReportDir();
        fileService.createNewDir(dirToSave);
        switch (Type.valueOf(type)) {
            case Fast5:
                generateFastQCHtmlReportFromFile(dirToSave);
                break;
            case FastQ:
                generateFastQCHtmlReportFromDir(dirToSave);
                break;
            default:
                throw new NotSupportedAnalysisTypeException();
        }
    }

    public String getHtmlReportPath() {
        final File reportDir = new File(getReportPrefixDir());

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

    public void downloadCurrentReport(final HttpServletResponse response) {
        setHtmlAndDownloadHeader("report", response);
        try {
            final OutputStream outputStream = response.getOutputStream();
            outputStream.write(fileService.getSummaryContent(getHtmlReportPath()));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new CsvDataCannotBeExportedException();
        }
    }

    public void saveFastQCHtmlReportFromDb(final Long id) {
        final String dirToSave = findHtmlReportDir();
        fileService.createNewDir(dirToSave);
        final Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(AnalysisNotFoundException::new);
        fileService.saveReport(analysis.getHtmlReport(), dirToSave + analysis.getName() + HTML);
    }

    private void setHtmlAndDownloadHeader(final String name, final HttpServletResponse response) {
        final String fileName = name + HTML;
        response.setContentType(HTML_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("FileName", fileName);
    }

    private String getReportPrefixDir() {
        return FileConsts.REPORT_DIR + applicationUserService.getCurrentUser().getId();
    }

    private String findHtmlReportDir() {
        final Long userId = applicationUserService.getCurrentUser().getId();
        return FileConsts.REPORT_DIR + userId + "/";
    }


    private void generateFastQCHtmlReportFromDir(final String dirToSave) {
        try {
            final List<String> filesPaths = Files.list(Paths.get(FileConsts.FILES_DIR))
                    .map(Path::toAbsolutePath)
                    .map(Object::toString)
                    .collect(Collectors.toList());
            final Process process = Runtime.getRuntime().exec(FAST_QC_TOOL_CMD + getConnectedFilesPaths(filesPaths) + " --outdir=" + dirToSave);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void generateFastQCHtmlReportFromFile(final String dirToSave) {
        try {
            final Process process = Runtime.getRuntime().exec(FAST_QC_TOOL_CMD + " " + FileConsts.FASTQ_FILE_FROM_FAST5_FOR_REPORT + " --outdir=" + dirToSave);
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
