package com.nanoporeqc.fast5analyse.controller;

import com.nanoporeqc.r.dto.ChartDto;
import com.nanoporeqc.r.dto.SummaryInfoDto;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.fast5analyse.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/r")
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping(value = "/charts/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChartDto getChartXY(@PathVariable("name") String name,
                               @RequestParam("xName") String xName,
                               @RequestParam("yNames[]") String[] yNames) {
        return statsService.getChartDataXY(RScriptEnum.getEnumByValue(name), xName, yNames);
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SummaryInfoDto> getSummaryInfo() {
        return statsService.getSummaryInformation();
    }
}
