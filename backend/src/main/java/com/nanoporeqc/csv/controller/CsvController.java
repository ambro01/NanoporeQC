package com.nanoporeqc.csv.controller;

import com.nanoporeqc.analysis.domain.ClusteringAlgorithm;
import com.nanoporeqc.csv.service.CsvService;
import com.nanoporeqc.r.enumeration.RDataEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/csv")
public class CsvController {

    private final CsvService csvService;

    public CsvController(final CsvService csvService) {
        this.csvService = csvService;
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity getCsvChartData(@PathVariable("name") final String name,
                                          @RequestParam("valuesNames[]") final List<String> valuesNames,
                                          HttpServletResponse response) {
        csvService.exportChartDataToCsv(name, valuesNames, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/info")
    public ResponseEntity getCsvSummaryInfo(final HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.SUMMARY_INFO, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/duplicated-sequences")
    public ResponseEntity getDuplicatedSequences(final HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.DUPLICATED_SEQUENCES, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/sequences-distribution")
    public ResponseEntity getReadsDistribution(final HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.SEQUENCES_DISTRIBUTION, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/reads-info")
    public ResponseEntity getReadsInfo(final HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.READS_INFO, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/clustering/{algorithm}")
    public ResponseEntity getReadsInfo(final HttpServletResponse response,
                                       @PathVariable("algorithm") final ClusteringAlgorithm algorithm,
                                       @RequestParam("clustersNumber") final Integer clusterNumber) {
        csvService.exportClusteringData(algorithm, clusterNumber, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/d2-detection")
    public ResponseEntity getD2Detection(final HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.D2_DETECTION, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/outliers-detection")
    public ResponseEntity getOutliersDetection(final HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.OUTLIERS_DETECTION, response);
        return new ResponseEntity(HttpStatus.OK);
    }

}