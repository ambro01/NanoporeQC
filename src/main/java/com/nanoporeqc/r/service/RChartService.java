package com.nanoporeqc.r.service;

import com.nanoporeqc.r.consts.RScriptsConst;
import com.nanoporeqc.r.domain.RScript;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.dto.ChartDto;
import com.nanoporeqc.r.dto.SummaryInfoDto;
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
            rService.updateRVariableData(xData);
            for (RVariable yData : yDataList) {
                rService.updateRVariableData(yData);
            }
        } finally {
            lock.unlock();
        }

        return ChartDto.builder()
                .xValues(xData.getRDataSet())
                .yValuesList(yDataList.stream().map(RVariable::getRDataSet).collect(Collectors.toList()))
                .build();
    }

    public List<SummaryInfoDto> getSummaryInformation() {
        final RScript readInfo = loadAllForRScript(RScriptEnum.READ_INFO);
        final RScript baseCalledTemplate = loadAllForRScript(RScriptEnum.BASE_CALLED_TEMPLATE);
        final RScript baseCalledComplement = loadAllForRScript(RScriptEnum.BASE_CALLED_COMPLEMENT);
        final RScript eventsData = loadAllForRScript(RScriptEnum.EVENTS_DATA);
        final String template = "template";
        final String complement = "complement";

        List<SummaryInfoDto> infoDtos = new ArrayList<>();

        for (Object idMain : readInfo.getRVariablesMap().get("id").getRDataSet()) {
            Integer id = (Integer) idMain - 1;
            SummaryInfoDto infoDto = SummaryInfoDto.builder()
                    .id(id + 1)
                    .fileName((String) getValueFromRDataSet(readInfo, "file", id))
                    .channelIndex((Integer) getValueFromRDataSet(readInfo, "channel", id))
                    .strandIndexInChannel((Integer) getValueFromRDataSet(readInfo, "read", id))
                    .startTime((Double) getValueFromRDataSet(eventsData, "start_time", id))
                    .duration((Double) getValueFromRDataSet(eventsData, "duration", id))
                    .eventsNo((Integer) getValueFromRDataSet(eventsData, "num_events", id))
                    .hasTemplate(template.equals(String.valueOf(getValueFromRDataSet(baseCalledTemplate, "strand_t", id))) ? "true" : "false")
                    .hasComplement(complement.equals(String.valueOf(getValueFromRDataSet(baseCalledComplement, "strand_c", id))) ? "true" : "false")
                    .eventsNoTemplate((Integer) getValueFromRDataSet(baseCalledTemplate, "num_events_t", id))
                    .eventsNoComplement((Integer) getValueFromRDataSet(baseCalledComplement, "num_events_c", id))
                    .is2d((Integer) getValueFromRDataSet(baseCalledTemplate, "full_2D_t", id) > 0 ? "true" : "false")
                    .build();

            infoDtos.add(infoDto);
        }
        return infoDtos;
    }

    public RScript loadAllForRScript(RScriptEnum rScriptEnum) {
        final RScript rScript = RScriptsConst.RScriptsMap.get(rScriptEnum);

        lock.lock();
        try {
            rService.evaluateRScript(rScriptEnum);
            rScript.getRVariablesMap().forEach((name, rVariable) -> {
                rService.updateRVariableData(rVariable);
            });
        } finally {
            lock.unlock();
        }

        return rScript;
    }

    private Object getValueFromRDataSet(RScript rScript, String variableName, int index) {
        List dataSet = rScript.getRVariablesMap().get(variableName).getRDataSet();
        return dataSet.size() > index ? dataSet.get(index) : null;
    }
}
