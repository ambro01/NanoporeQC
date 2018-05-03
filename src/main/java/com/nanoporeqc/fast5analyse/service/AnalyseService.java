package com.nanoporeqc.fast5analyse.service;

import com.nanoporeqc.fast5analyse.domain.Analyse;
import com.nanoporeqc.fast5analyse.dto.AnalyseDto;
import com.nanoporeqc.fast5analyse.exceptions.AnalyseNotFoundException;
import com.nanoporeqc.fast5analyse.repository.AnalyseRepository;
import com.nanoporeqc.fast5file.consts.FileConsts;
import com.nanoporeqc.fast5file.service.FileService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnalyseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private RService rService;
    private FileService fileService;
    private AnalyseRepository analyseRepository;
    private ApplicationUserService applicationUserService;
    private ModelMapper modelMapper;

    @Autowired
    public AnalyseService(final RService rService,
                          final FileService fileService,
                          final AnalyseRepository analyseRepository,
                          final ApplicationUserService applicationUserService,
                          final ModelMapper modelMapper) {
        this.rService = rService;
        this.fileService = fileService;
        this.analyseRepository = analyseRepository;
        this.applicationUserService = applicationUserService;
        this.modelMapper = modelMapper;
    }

    public void runNewAnalyse(AnalyseDto analyseDto) {
        LOGGER.info("Analyse: Running new analyse");
        rService.readFast5FilesFromDir(FileConsts.FAST5FILES_DIR);

        try {
            FileUtils.cleanDirectory(new File(FileConsts.FAST5FILES_DIR));
            LOGGER.info("Analyse: Saving summary file");
            rService.saveSummaryToFile();
            saveNewAnalyse(analyseDto);
        } catch (IOException | UserNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void runOldAnalyse(final Long id) {
        LOGGER.info("Analyse: Running old analyse, id: " + id);
        try {
            Analyse analyse = analyseRepository.findById(id)
                    .orElseThrow(AnalyseNotFoundException::new);

            fileService.saveSummaryToFile(analyse.getContent());
            rService.loadSummaryFromFile();
        } catch (IOException | SQLException | AnalyseNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void saveNewAnalyse(AnalyseDto analyseDto) throws UserNotFoundException, SQLException {
        Analyse analyse = Analyse.builder()
                .name(analyseDto.getName())
                .comment(analyseDto.getComment())
                .runTime(LocalDateTime.now())
                .content(new SerialBlob(fileService.getSummaryContent()))
                .user(applicationUserService.getCurrentUser())
                .build();

        analyseRepository.save(analyse);
        LOGGER.info("Analyse: Analyse " + analyseDto.getName() + " was saved");
    }

    public List<AnalyseDto> getAnalysesForCurrentUser() throws UserNotFoundException {
        List<Analyse> analyses = analyseRepository.findAllByUserId(applicationUserService.getCurrentUser().getId());
        return analyses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteAnalyse(final Long id) {
        analyseRepository.findById(id)
                .ifPresent(analyse -> analyseRepository.delete(analyse));
    }

    private AnalyseDto convertToDto(final Analyse analyse) {
        AnalyseDto analyseDto = modelMapper.map(analyse, AnalyseDto.class);
        analyseDto.setRunTime(convertLocalDateTimeToString(analyse.getRunTime()));
        return analyseDto;
    }

    private Analyse convertToEntity(final AnalyseDto analyseDto) {
        Analyse analyse = modelMapper.map(analyseDto, Analyse.class);
        analyse.setRunTime(convertStringToLocalDateTime(analyseDto.getRunTime()));
        return analyse;
    }

    private String convertLocalDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    private LocalDateTime convertStringToLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }
}
