package com.nanoporeqc.fast5analyse.controller;

import com.nanoporeqc.fast5analyse.dto.AnalyseDto;
import com.nanoporeqc.fast5analyse.service.AnalyseService;
import com.nanoporeqc.fast5analyse.service.StatsService;
import com.nanoporeqc.r.dto.ChartDto;
import com.nanoporeqc.r.dto.SummaryInfoDto;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.user.exceptions.UserNotFoundException;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/analyse")
public class AnalyseController {

    @Autowired
    private AnalyseService analyseService;
    @Autowired
    private StatsService statsService;

    @PostMapping(value = "/new")
    public ResponseEntity runNewAnalyse(@RequestBody AnalyseDto analyseDto) throws IOException {
        analyseService.runNewAnalyse(analyseDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity runNewAnalyse(@PathVariable("id") Long id) {
        analyseService.runOldAnalyse(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/current-user")
    public List<AnalyseDto> getAnalysesForCurrentUser() throws UserNotFoundException {
        return analyseService.getAnalysesForCurrentUser();
    }

    @PostMapping(value = "/delete/{id}")
    public void deleteAnalyse(@PathVariable("id") Long id) {
        analyseService.deleteAnalyse(id);
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