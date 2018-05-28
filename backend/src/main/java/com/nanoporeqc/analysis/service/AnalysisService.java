package com.nanoporeqc.analysis.service;

import com.nanoporeqc.analysis.domain.Analysis;
import com.nanoporeqc.analysis.domain.Type;
import com.nanoporeqc.analysis.dto.AnalysisDto;
import com.nanoporeqc.analysis.repository.AnalysisRepository;
import com.nanoporeqc.exceptions.AnalysisCannotBeSavedException;
import com.nanoporeqc.exceptions.AnalysisNotFoundException;
import com.nanoporeqc.exceptions.NotSupportedAnalysisTypeException;
import com.nanoporeqc.exceptions.UserNotFoundException;
import com.nanoporeqc.file.consts.FileConsts;
import com.nanoporeqc.file.service.FileService;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.r.service.RService;
import com.nanoporeqc.user.service.ApplicationUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialBlob;
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

    private final RService rService;
    private final FileService fileService;
    private final AnalysisRepository analysisRepository;
    private final ApplicationUserService applicationUserService;
    private final ModelMapper modelMapper;
    private final ReentrantLock lockAnalysis = new ReentrantLock();

    @Autowired
    public AnalysisService(final RService rService,
                           final FileService fileService,
                           final AnalysisRepository analysisRepository,
                           final ApplicationUserService applicationUserService,
                           final ModelMapper modelMapper) {
        this.rService = rService;
        this.fileService = fileService;
        this.analysisRepository = analysisRepository;
        this.applicationUserService = applicationUserService;
        this.modelMapper = modelMapper;
    }

    public void runNewAnalysis(final AnalysisDto analysisDto) {
        final RScriptEnum rScriptEnum = rService.getSummaryFromDirScript(analysisDto.getType());
        LOGGER.info("Analysis: Running new analyse of type: " + analysisDto.getType());
        rService.loadFilesFromDirToR(rScriptEnum);
    }

    public void saveNewAnalysis(final AnalysisDto analysisDto) {
        lockAnalysis.lock();
        try {
            fileService.cleanDirectory(FileConsts.SUMMARY_DIR);
            LOGGER.info("Analysis: Saving summary file of type: " + analysisDto.getType());
            rService.saveSummaryToFile(analysisDto.getType());
            saveAnalysis(analysisDto);
        } finally {
            lockAnalysis.unlock();
        }
    }

    public void runOldAnalysis(final Long id, final String sourceType) {
        lockAnalysis.lock();
        try {
            LOGGER.info("Running old analyse, id: " + id);
            final Analysis analysis = analysisRepository.findById(id)
                    .orElseThrow(AnalysisNotFoundException::new);
            fileService.saveSummaryToFile(analysis.getContent());
            rService.loadSummaryFromFile(sourceType);
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
        runOldAnalysis(id, Type.Fast5.name());
        rService.loadSummaryAFromSummaryB();
    }

    private AnalysisDto convertAnalysisToDto(final Analysis analysis) {
        final AnalysisDto analysisDto = modelMapper.map(analysis, AnalysisDto.class);
        analysisDto.setRunTime(convertLocalDateTimeToString(analysis.getRunTime()));
        return analysisDto;
    }

    private Analysis convertDtoToAnalysis(final AnalysisDto analysisDto) {
        final Analysis analysis = modelMapper.map(analysisDto, Analysis.class);
        analysis.setRunTime(convertStringToLocalDateTime(analysisDto.getRunTime()));
        return analysis;
    }

    private String convertLocalDateTimeToString(final LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    private LocalDateTime convertStringToLocalDateTime(final String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }

    private Analysis prepareAnalysisToSave(final AnalysisDto analysisDto) {
        try {
            return Analysis.builder()
                    .name(analysisDto.getName())
                    .comment(analysisDto.getComment())
                    .runTime(LocalDateTime.now())
                    .type(Type.valueOf(analysisDto.getType()))
                    .content(new SerialBlob(fileService.getSummaryContent()))
                    .user(applicationUserService.getCurrentUser())
                    .build();
        } catch (UserNotFoundException | SQLException e) {
            throw new AnalysisCannotBeSavedException();
        }
    }
}
