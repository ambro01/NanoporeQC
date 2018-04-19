package com.nanoporeqc.r.controller;

import com.nanoporeqc.r.dto.ChartDto;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.r.service.RChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/r")
public class RController {

    @Autowired
    private RChartService rChartService;

    @CrossOrigin
    @RequestMapping(value = "/charts/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChartDto getChartXY(@PathVariable("name") String name,
                               @RequestParam("id") String id,
                               @RequestParam("xName") String xName,
                               @RequestParam("yNames[]") String[] yNames) {
        return rChartService.getChartDataXY(RScriptEnum.getEnumByValue(name), xName, yNames);
    }
}
