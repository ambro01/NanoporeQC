package com.nanoporeqc.csv.controller;

import com.nanoporeqc.csv.service.CsvService;
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

    @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCsvChartData(@PathVariable("name") String name,
                                          @RequestParam("xName") String xName,
                                          @RequestParam("yNames[]") List<String> yNames,
                                          HttpServletResponse response) {
        csvService.exportChartDataToCsv(name, xName, yNames, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCsvSummaryInfo(HttpServletResponse response) {
        csvService.exportReadsToCsv(RScriptEnum.READ_INFO, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/duplicated-sequences", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getDuplicatedSequences(HttpServletResponse response) {
        csvService.exportReadsToCsv(RScriptEnum.DUPLICATED_READS, response);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/reads-distribution", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getReadsDistribution(HttpServletResponse response) {
        csvService.exportReadsToCsv(RScriptEnum.READ_DISTRIBUTION, response);
        return new ResponseEntity(HttpStatus.OK);
    }

}