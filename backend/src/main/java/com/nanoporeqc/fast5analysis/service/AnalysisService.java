package com.nanoporeqc.fast5analysis.service;

import com.nanoporeqc.fast5analysis.domain.Analysis;
import com.nanoporeqc.fast5analysis.domain.Type;
import com.nanoporeqc.fast5analysis.dto.AnalysisDto;
import com.nanoporeqc.fast5analysis.exceptions.AnalysisNotFoundException;
import com.nanoporeqc.fast5analysis.repository.AnalysisRepository;
import com.nanoporeqc.fast5file.consts.FileConsts;
import com.nanoporeqc.fast5file.service.FileService;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.r.service.RService;
import com.nanoporeqc.user.exceptions.UserNotFoundException;
import com.nanoporeqc.user.service.ApplicationUserService;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        LOGGER.info("Analysis: Running new analyse");
        switch (Type.valueOf(analysisDto.getType())) {
            case Fast5:
                rService.loadFilesFromDirToR(FileConsts.FILES_DIR, RScriptEnum.READ_FAST5_SUMMARY_FROM_DIR);
            case FastQ:
                rService.loadFilesFromDirToR(FileConsts.FILES_DIR, RScriptEnum.READ_FASTQ_SUMMARY_FROM_DIR);
        }
        try {
            FileUtils.cleanDirectory(new File(FileConsts.FILES_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveNewAnalysis(final AnalysisDto analysisDto) {
        try {
            FileUtils.cleanDirectory(new File(FileConsts.SUMMARY_DIR));
            LOGGER.info("Analysis: Saving summary file");
            rService.saveSummaryToFile();
            saveAnalysis(analysisDto);
        } catch (IOException | UserNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void runOldAnalysis(final Long id, final String type) {
        LOGGER.info("Analysis: Running old analyse, id: " + id);
        try {
            Analysis analysis = analysisRepository.findById(id)
                    .orElseThrow(AnalysisNotFoundException::new);
            fileService.saveSummaryToFile(analysis.getContent());
            rService.loadSummaryFromFile(type);
        } catch (IOException | SQLException | AnalysisNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void saveAnalysis(final AnalysisDto analysisDto) throws UserNotFoundException, SQLException {
        Analysis analysis = Analysis.builder()
                .name(analysisDto.getName())
                .comment(analysisDto.getComment())
                .runTime(LocalDateTime.now())
                .type(Type.valueOf(analysisDto.getType()))
                .content(new SerialBlob(fileService.getSummaryContent()))
                .user(applicationUserService.getCurrentUser())
                .build();

        analysisRepository.save(analysis);
        LOGGER.info("Analysis: Analysis " + analysisDto.getName() + " was saved");
    }

    public List<AnalysisDto> getAnalysesForCurrentUser() throws UserNotFoundException {
        List<Analysis> analyses = analysisRepository.findAllByUserId(applicationUserService.getCurrentUser().getId());
        return analyses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteAnalysis(final Long id) {
        analysisRepository.findById(id)
                .ifPresent(analysisRepository::delete);
    }

    public void runFastQFromFast5(final Long id) {
        runOldAnalysis(id, Type.Fast5.name());
        rService.loadFilesFromDirToR(FileConsts.FILES_DIR, RScriptEnum.READ_FASTQ_SUMMARY_FROM_FAST5_SUMMARY);
    }

    private AnalysisDto convertToDto(final Analysis analysis) {
        final AnalysisDto analysisDto = modelMapper.map(analysis, AnalysisDto.class);
        analysisDto.setRunTime(convertLocalDateTimeToString(analysis.getRunTime()));
        return analysisDto;
    }

    private Analysis convertToEntity(final AnalysisDto analysisDto) {
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
}
