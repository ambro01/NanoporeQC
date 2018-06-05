package com.nanoporeqc.analysis.controller;

import com.nanoporeqc.analysis.dto.AnalysisDto;
import com.nanoporeqc.analysis.dto.DuplicatedSequenceDto;
import com.nanoporeqc.analysis.dto.ReadDistributionDto;
import com.nanoporeqc.analysis.service.AnalysisService;
import com.nanoporeqc.analysis.service.StatsService;
import com.nanoporeqc.analysis.dto.ChartDto;
import com.nanoporeqc.analysis.dto.SummaryInfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final StatsService statsService;

    public AnalysisController(final AnalysisService analysisService,
                              final StatsService statsService) {
        this.analysisService = analysisService;
        this.statsService = statsService;
    }

    @PostMapping(value = "/run-new")
    public ResponseEntity runAnalysis(@RequestBody final AnalysisDto analysisDto) {
        analysisService.runNewAnalysis(analysisDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/save-new")
    public ResponseEntity saveNewAnalysis(@RequestBody final AnalysisDto analysisDto) {
        analysisService.saveNewAnalysis(analysisDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity runOldAnalysis(@PathVariable("id") Long id, @RequestParam("type") String type) {
        analysisService.runOldAnalysis(id, type);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/from-fast5/{id}")
    public ResponseEntity runFastQFromFast5(@PathVariable("id") Long id) {
        analysisService.runFastQFromFast5(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/current-user")
    public List<AnalysisDto> getAnalysesForCurrentUser() {
        return analysisService.getAnalysesForCurrentUser();
    }

    @PostMapping(value = "/delete/{id}")
    public void deleteAnalysis(@PathVariable("id") Long id) {
        analysisService.deleteAnalysis(id);
    }

    @GetMapping(value = "/stats/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChartDto getChartXY(@PathVariable("name") String name,
                               @RequestParam("xName") String xName,
                               @RequestParam("yNames[]") List<String> yNames) {
        return statsService.getChartDataXY(name, xName, yNames);
    }

    @GetMapping(value = "/stats/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SummaryInfoDto> getSummaryInfo() {
        return statsService.getSummaryInformation();
    }

    @GetMapping(value = "/stats/duplicated-sequences", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DuplicatedSequenceDto> getDuplicatedSequences() {
        return statsService.getDuplicatedSequences();
    }

    @GetMapping(value = "/stats/reads-distribution", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReadDistributionDto> getReadsDistribution() {
        return statsService.getReadsDistribution();
    }

    @GetMapping(value = "/amount/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getAnalysesAmount(@PathVariable("type") String type) {
        return analysisService.getAnalysesAmount(type);
    }

    @GetMapping(value = "/last/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLastAnalysisTime(@PathVariable("type") String type) {
        return analysisService.getLastAnalysisTime(type);
    }

}