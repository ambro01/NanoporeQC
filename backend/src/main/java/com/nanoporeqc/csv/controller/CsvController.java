package com.nanoporeqc.csv.controller;

import com.nanoporeqc.csv.service.CsvService;
import com.nanoporeqc.r.domain.RData;
import com.nanoporeqc.r.enumeration.RDataEnum;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity getCsvChartData(@PathVariable("name") String name,
                                          @RequestParam("valuesNames[]") List<String> valuesNames,
                                          HttpServletResponse response) {
        csvService.exportChartDataToCsv(name, valuesNames, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/info")
    public ResponseEntity getCsvSummaryInfo(HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.SUMMARY_INFO, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/duplicated-sequences")
    public ResponseEntity getDuplicatedSequences(HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.DUPLICATED_SEQUENCES, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/sequences-distribution")
    public ResponseEntity getReadsDistribution(HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.SEQUENCES_DISTRIBUTION, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/reads-info")
    public ResponseEntity getReadsInfo(HttpServletResponse response) {
        csvService.exportReadsToCsv(RDataEnum.READS_INFO, response);
        return new ResponseEntity(HttpStatus.OK);
    }

}