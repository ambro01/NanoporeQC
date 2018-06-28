package com.nanoporeqc.analysis.service;

import com.nanoporeqc.analysis.domain.Analysis;
import com.nanoporeqc.analysis.domain.Type;
import com.nanoporeqc.analysis.dto.AnalysisDto;
import com.nanoporeqc.analysis.repository.AnalysisRepository;
import com.nanoporeqc.exceptions.AnalysisCannotBeSavedException;
import com.nanoporeqc.exceptions.AnalysisNotFoundException;
import com.nanoporeqc.exceptions.UserNotFoundException;
import com.nanoporeqc.file.consts.FileConsts;
import com.nanoporeqc.file.service.FileService;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.r.service.RService;
import com.nanoporeqc.user.service.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String PORETOOLS_CMD = "poretools fastq --type best";

    private final RService rService;
    private final FileService fileService;
    private final ReportService reportService;
    private final AnalysisRepository analysisRepository;
    private final ApplicationUserService applicationUserService;
    private final ModelMapper modelMapper;
    private final ReentrantLock lockAnalysis = new ReentrantLock();

    public AnalysisService(final RService rService,
                           final FileService fileService,
                           final ReportService reportService,
                           final AnalysisRepository analysisRepository,
                           final ApplicationUserService applicationUserService,
                           final ModelMapper modelMapper) {
        this.rService = rService;
        this.fileService = fileService;
        this.reportService = reportService;
        this.analysisRepository = analysisRepository;
        this.applicationUserService = applicationUserService;
        this.modelMapper = modelMapper;
    }

    public void runNewAnalysis(final AnalysisDto analysisDto) {
        fileService.cleanDirectory(FileConsts.REPORT_DIR);
        LOGGER.info("Analysis: Running new analyse of type: " + analysisDto.getType());
        rService.loadFilesFromDirToR(rService.getSummaryFromDirScript(analysisDto.getType()));
        rService.evaluateRScript(rService.getGenerateDataScript(analysisDto.getType()));
        reportService.saveLocallyFastQCHtmlReport(analysisDto.getType());
        generateFastQDataFromFast5(analysisDto);
        fileService.cleanDirectory(FileConsts.FILES_DIR);
    }

    public void saveNewAnalysis(final AnalysisDto analysisDto) {
        lockAnalysis.lock();
        try {
            final Type type = Type.valueOf(analysisDto.getType());
            final String summaryPath = Type.Fast5.equals(type) ? FileConsts.SUMMARY_FAST5_FILE : FileConsts.SUMMARY_FASTQ_FILE;
            fileService.cleanDirectory(FileConsts.SUMMARY_DIR);
            LOGGER.info("Analysis: Saving summary file of type: " + analysisDto.getType());
            rService.saveSummaryToFile(type, summaryPath);
            if (Type.Fast5.equals(Type.valueOf(analysisDto.getType()))) {
                rService.evaluateRScript(RScriptEnum.SAVE_SUMMARY_FASTQ);
            }
            saveAnalysis(analysisDto);
        } finally {
            lockAnalysis.unlock();
        }
    }

    public void runOldAnalysis(final Long id, final String sourceType) {
        LOGGER.info("Running old analyse, id: " + id);
        final RScriptEnum rScriptEnum = rService.getReadSummaryScript(sourceType);
        final Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(AnalysisNotFoundException::new);
        loadMainSummary(analysis, rScriptEnum);
    }

    private void loadMainSummary(final Analysis analysis, final RScriptEnum rScriptEnum) {
        lockAnalysis.lock();
        try {
            final String filePath = Type.Fast5.equals(analysis.getType()) ? FileConsts.SUMMARY_FAST5_FILE : FileConsts.SUMMARY_FASTQ_FILE;
            fileService.saveSummaryToFile(analysis.getMainSummary(), filePath);
            rService.loadSummaryFromFile(rScriptEnum, filePath);
        } finally {
            lockAnalysis.unlock();
        }
    }

    private void loadAdditionalSummary(final Analysis analysis, final RScriptEnum rScriptEnum) {
        lockAnalysis.lock();
        try {
            final String filePath = Type.Fast5.equals(analysis.getType()) ? FileConsts.SUMMARY_FAST5_FILE : FileConsts.SUMMARY_FASTQ_FILE;
            fileService.saveSummaryToFile(analysis.getAdditionalSummary(), filePath);
            rService.loadSummaryFromFile(rScriptEnum, filePath);
        } finally {
            lockAnalysis.unlock();
        }
    }

    @Transactional
    public void saveAnalysis(final AnalysisDto analysisDto) {
        final Analysis analysis = prepareAnalysisToSave(analysisDto);
        analysisRepository.save(analysis);
        LOGGER.info("Analysis " + analysisDto.getName() + " was saved");
    }

    public List<AnalysisDto> getAnalysesForCurrentUser() {
        final List<Analysis> analyses = analysisRepository.findAllByUserId(applicationUserService.getCurrentUser().getId());
        return analyses.stream()
                .map(this::convertAnalysisToDto)
                .collect(Collectors.toList());
    }

    public void deleteAnalysis(final Long id) {
        analysisRepository.findById(id).ifPresent(analysisRepository::delete);
    }

    public void runFastQFromFast5(final Long id) {
        final RScriptEnum rScriptEnum = rService.getReadSummaryScript(Type.FastQ.name());
        final Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(AnalysisNotFoundException::new);
        loadAdditionalSummary(analysis, rScriptEnum);
        reportService.saveFastQCHtmlReportFromDb(analysis);
        fileService.cleanDirectory(FileConsts.FILES_DIR);
    }

    public Long getAnalysesAmount(final String type) {
        return analysisRepository.countByType(Type.valueOf(type));
    }

    public String getLastAnalysisTime(final String type) {
        final LocalDateTime analysisTime = analysisRepository.findFirstByTypeOrderByRunTimeDesc(Type.valueOf(type)).getRunTime();
        return analysisTime.format(DATE_TIME_FORMATTER);
    }

    public void downloadHtmlReport(final Long id, final HttpServletResponse response) {
        final Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(AnalysisNotFoundException::new);
        reportService.downloadReport(analysis, response);
    }

    public void downloadCurrentHtmlReport(final HttpServletResponse response) {
        reportService.downloadCurrentReport(response);
    }

    private void runPoretoolsFast5ToFastQ() {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(PORETOOLS_CMD)
                    .append(" ")
                    .append(FileConsts.FILES_DIR);
            final Process process = Runtime.getRuntime().exec(sb.toString());
            final File file = fileService.createNewFile(FileConsts.FASTQ_FILE_FROM_FAST5);
            final InputStream inputStream = process.getInputStream();
            java.nio.file.Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            IOUtils.closeQuietly(inputStream);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private AnalysisDto convertAnalysisToDto(final Analysis analysis) {
        final AnalysisDto analysisDto = modelMapper.map(analysis, AnalysisDto.class);
        analysisDto.setRunTime(convertLocalDateTimeToString(analysis.getRunTime()));
        analysisDto.setHasHtmlReport(analysis.getHtmlReport() != null);
        analysisDto.setFastQFromFast5(analysis.getParentAnalysisId() != null);
        return analysisDto;
    }

    private String convertLocalDateTimeToString(final LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    private Analysis prepareAnalysisToSave(final AnalysisDto analysisDto) {
        try {
            final Analysis analysis = Analysis.builder()
                    .name(analysisDto.getName())
                    .comment(analysisDto.getComment())
                    .runTime(LocalDateTime.now())
                    .type(Type.valueOf(analysisDto.getType()))
                    .parentAnalysisId(analysisDto.getParentAnalysisId())
                    .mainSummary(new SerialBlob(fileService.getSummaryContent(FileConsts.SUMMARY_FAST5_FILE)))
                    .user(applicationUserService.getCurrentUser())
                    .htmlReport(new SerialBlob(fileService.getSummaryContent(reportService.getHtmlReportPath())))
                    .build();
            if (Type.Fast5.equals(Type.valueOf(analysisDto.getType()))) {
                analysis.setAdditionalSummary(new SerialBlob(fileService.getSummaryContent(FileConsts.SUMMARY_FASTQ_FILE)));
            }
            return analysis;
        } catch (UserNotFoundException | SQLException e) {
            throw new AnalysisCannotBeSavedException();
        } finally {
            fileService.cleanDirectory(FileConsts.SUMMARY_DIR);
        }
    }

    private void generateFastQDataFromFast5(final AnalysisDto analysisDto) {
        if (Type.Fast5.equals(Type.valueOf(analysisDto.getType()))) {
            runPoretoolsFast5ToFastQ();
            rService.evaluateRScript(RScriptEnum.READ_FASTQ_SUMMARY_FROM_DIR);
            rService.evaluateRScript(RScriptEnum.GENERATE_DATA_FASTQ);
        }
    }

}
