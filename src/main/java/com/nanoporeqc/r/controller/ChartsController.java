package com.nanoporeqc.r.controller;

import com.nanoporeqc.ChartDto;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.r.service.RChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/charts")
public class ChartsController {

    @Autowired
    private RChartService rChartService;

    @CrossOrigin
    @RequestMapping(value = "/{name}/{x}/{y}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ChartDto getChartXY(@PathVariable("name") String name, @PathVariable("x") String x, @PathVariable("y") String y) {
        return rChartService.getChartDataXY(RScriptEnum.getEnumByValue(name), x, y);
    }
}
