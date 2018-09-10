package com.nanoporeqc.analysis.controller;

import com.nanoporeqc.analysis.domain.ClusteringAlgorithm;
import com.nanoporeqc.analysis.domain.Type;
import com.nanoporeqc.analysis.dto.AnalysisDto;
import com.nanoporeqc.analysis.dto.ChartDto;
import com.nanoporeqc.analysis.dto.ClusteringReadsDto;
import com.nanoporeqc.analysis.dto.D2DetectionDto;
import com.nanoporeqc.analysis.dto.DuplicatedSequencesDto;
import com.nanoporeqc.analysis.dto.OutlierDto;
import com.nanoporeqc.analysis.dto.ReadsInfoDto;
import com.nanoporeqc.analysis.dto.SequencesDistributionDto;
import com.nanoporeqc.analysis.dto.SummaryInfoDto;
import com.nanoporeqc.analysis.service.AnalysisService;
import com.nanoporeqc.analysis.service.StatsService;
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

import javax.servlet.http.HttpServletResponse;
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
    public ResponseEntity runOldAnalysis(@PathVariable("id") final Long id, @RequestParam("type") final Type type) {
        analysisService.runOldAnalysis(id, type);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/from-fast5/{id}")
    public ResponseEntity runFastQFromFast5(@PathVariable("id") final Long id) {
        analysisService.runFastQFromFast5(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/current-user")
    public List<AnalysisDto> getAnalysesForCurrentUser() {
        return analysisService.getAnalysesForCurrentUser();
    }

    @PostMapping(value = "/delete/{id}")
    public void deleteAnalysis(@PathVariable("id") final Long id) {
        analysisService.deleteAnalysis(id);
    }

    @GetMapping(value = "/stats/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChartDto getChartXY(@PathVariable("name") final String name,
                               @RequestParam("valuesNames[]") final List<String> valuesNames) {
        return statsService.getChartDataXY(name, valuesNames);
    }

    @GetMapping(value = "/stats/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SummaryInfoDto> getSummaryInfo() {
        return statsService.getSummaryInformation();
    }

    @GetMapping(value = "/stats/duplicated-sequences", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DuplicatedSequencesDto> getDuplicatedSequences() {
        return statsService.getDuplicatedSequences();
    }

    @GetMapping(value = "/stats/sequences-distribution", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SequencesDistributionDto> getReadsDistribution() {
        return statsService.getSequencesDistribution();
    }

    @GetMapping(value = "/stats/reads-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReadsInfoDto> getReadsInfo() {
        return statsService.getReadsInfo();
    }

    @GetMapping(value = "/amount/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getAnalysesAmount(@PathVariable("type") final Type type) {
        return analysisService.getAnalysesAmount(type);
    }

    @GetMapping(value = "/success-ratio/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Double getSuccessAnalysesRatio(@PathVariable("type") final Type type) {
        return analysisService.getSuccessAnalysesRatio(type);
    }

    @GetMapping(value = "/last/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLastAnalysisTime(@PathVariable("type") final Type type) {
        return analysisService.getLastAnalysisTime(type);
    }

    @GetMapping(value = "/last-success/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLastSuccessAnalysisTime(@PathVariable("type") final Type type) {
        return analysisService.getLastSuccessAnalysisTime(type);
    }

    @GetMapping(value = "/download-report/{id}")
    public ResponseEntity downloadHtmlReport(@PathVariable("id") final Long id, final HttpServletResponse response) {
        analysisService.downloadHtmlReport(id, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/download-current-report")
    public ResponseEntity downloadCurrentHtmlReport(final HttpServletResponse response) {
        analysisService.downloadCurrentHtmlReport(response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/clustering/reads/{algorithm}/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClusteringReadsDto> getClusteringReads(@PathVariable("type") final Type type,
                                                       @PathVariable("algorithm") final ClusteringAlgorithm clusteringAlgorithm,
                                                       @RequestParam("clustersNumber") final Integer clustersNumber) {
        return statsService.runClustering(type, clusteringAlgorithm, clustersNumber);
    }

    @GetMapping(value = "/d2-detection/reads/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<D2DetectionDto> getClusteringReads(@PathVariable("type") final Type type) {
        return statsService.runD2Detection(type);
    }

    @GetMapping(value = "/outliers-detection/reads/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OutlierDto> getClusteringReads(@PathVariable("type") final Type type,
                                               @RequestParam("outliersProportion") final Double outliersProportion) {
        return statsService.runOutliersDetection(type, outliersProportion);
    }

}