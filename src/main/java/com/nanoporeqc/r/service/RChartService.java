package com.nanoporeqc.r.service;

import com.nanoporeqc.ChartDto;
import com.nanoporeqc.r.config.RConfiguration;
import com.nanoporeqc.r.consts.RScriptsConst;
import com.nanoporeqc.r.domain.RFast5Resource;
import com.nanoporeqc.r.domain.RScript;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class RChartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RChartService.class);

    private RService rService;

    @Autowired
    public RChartService(RService rService) {
        this.rService = rService;
    }


    public ChartDto getChartDataXY(RScriptEnum rScriptEnum, String xDataName, String yDataName) {

        RScript rScript = RScriptsConst.RScriptsMap.get(rScriptEnum);

        URL resource = RConfiguration.class.getResource("/r_scripts/" + rScript.getName().getFileName());

        rService.evaluateRScript(resource);

        RVariable xData = rScript.getRVariablesMap().get(xDataName);
        RVariable yData = rScript.getRVariablesMap().get(yDataName);


        xData.setRDataSet(rService.getDataSetFromR(xData));
        yData.setRDataSet(rService.getDataSetFromR(yData));

        ChartDto chartDto = new ChartDto();
        chartDto.setXValues(xData.getRDataSet());
        chartDto.setYValues(yData.getRDataSet());

        return chartDto;
    }
}
