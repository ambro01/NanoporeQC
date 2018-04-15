package com.nanoporeqc.r.service;

import com.nanoporeqc.ChartDto;
import com.nanoporeqc.r.consts.RScriptsConst;
import com.nanoporeqc.r.domain.RScript;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class RChartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RChartService.class);

    private final RService rService;

    private ReentrantLock lock = new ReentrantLock();

    @Autowired
    public RChartService(final RService rService) {
        this.rService = rService;
    }


    public ChartDto getChartDataXY(RScriptEnum rScriptEnum, String xName, String[] yNames) {

        final RScript rScript = RScriptsConst.RScriptsMap.get(rScriptEnum);

        RVariable xData = rScript.getRVariablesMap().get(xName);
        List<RVariable> yDataList = new ArrayList<>();
        for (String yName : yNames) {
            yDataList.add(rScript.getRVariablesMap().get(yName));
        }

        lock.lock();
        try {
            rService.evaluateRScript(rScriptEnum);
            xData.setRDataSet(rService.getDataSetFromR(xData));
            for (RVariable yData : yDataList) {
                yData.setRDataSet(rService.getDataSetFromR(yData));
            }
        } finally {
            lock.unlock();
        }

        return ChartDto.builder()
                .xValues(xData.getRDataSet())
                .yValuesList(yDataList.stream().map(RVariable::getRDataSet).collect(Collectors.toList()))
                .build();
    }
}
