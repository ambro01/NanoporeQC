package com.nanoporeqc.fast5analysis.service;

import com.nanoporeqc.fast5analysis.dto.ChartDto;
import com.nanoporeqc.fast5analysis.dto.DuplicatedSequenceDto;
import com.nanoporeqc.fast5analysis.dto.ReadDistributionDto;
import com.nanoporeqc.fast5analysis.dto.SummaryInfoDto;
import com.nanoporeqc.r.consts.RScriptsConst;
import com.nanoporeqc.r.domain.RScript;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.r.service.RService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsService.class);

    private final RService rService;

    private final ReentrantLock lock = new ReentrantLock();

    public StatsService(final RService rService) {
        this.rService = rService;
    }

    public ChartDto getChartDataXY(final RScriptEnum rScriptEnum, final String xName, final String[] yNames) {
        final RScript rScript = RScriptsConst.RScriptsMap.get(rScriptEnum);

        final RVariable xData = rScript.getRVariablesMap().get(xName);
        final List<RVariable> yDataList = new ArrayList<>();

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

        final List<SummaryInfoDto> infoDtoList = new ArrayList<>();

        for (int i = 0; i < readInfo.getRVariablesMap().get("id").getRDataSet().size(); ++i) {
            final SummaryInfoDto infoDto = SummaryInfoDto.builder()
                    .id(i + 1)
                    .fileName((String) getValueFromRDataSet(readInfo, "file", i))
                    .channelIndex((Integer) getValueFromRDataSet(readInfo, "channel", i))
                    .strandIndexInChannel((Integer) getValueFromRDataSet(readInfo, "read", i))
                    .startTime((Double) getValueFromRDataSet(eventsData, "start_time", i))
                    .duration((Double) getValueFromRDataSet(eventsData, "duration", i))
                    .eventsNo((Integer) getValueFromRDataSet(eventsData, "num_events", i))
                    .hasTemplate(template.equals(String.valueOf(getValueFromRDataSet(baseCalledTemplate, "strand_t", i))) ? "true" : "false")
                    .hasComplement(complement.equals(String.valueOf(getValueFromRDataSet(baseCalledComplement, "strand_c", i))) ? "true" : "false")
                    .eventsNoTemplate((Integer) getValueFromRDataSet(baseCalledTemplate, "num_events_t", i))
                    .eventsNoComplement((Integer) getValueFromRDataSet(baseCalledComplement, "num_events_c", i))
                    .is2d(Optional.ofNullable(getValueFromRDataSet(baseCalledTemplate, "full_2D_t", i))
                            .map(o -> (Integer) o).map(integer -> integer > 0).map(aBoolean -> aBoolean ? "true" : "false").orElse("false"))
                    .build();

            infoDtoList.add(infoDto);
        }
        return infoDtoList;
    }

    public List<DuplicatedSequenceDto> getDuplicatedSequences() {
        final RScript rScript = loadAllForRScript(RScriptEnum.DUPLICATED_READS);
        final List<DuplicatedSequenceDto> duplicatedSequenceDtoList = new ArrayList<>();

        for (int i = 0; i < rScript.getRVariablesMap().get("count").getRDataSet().size(); ++i) {
            final DuplicatedSequenceDto duplicatedSequenceDto = DuplicatedSequenceDto.builder()
                    .count((Integer) getValueFromRDataSet(rScript, "count", i))
                    .sequence((String) getValueFromRDataSet(rScript, "sequence", i))
                    .build();

            duplicatedSequenceDtoList.add(duplicatedSequenceDto);
        }
        return duplicatedSequenceDtoList;
    }

    public List<ReadDistributionDto> getReadsDistribution() {
        final RScript rScript = loadAllForRScript(RScriptEnum.READ_DISTRIBUTION);
        final List<ReadDistributionDto> readDistributionDtoList = new ArrayList<>();

        for (int i = 0; i < rScript.getRVariablesMap().get("occurrences").getRDataSet().size(); ++i) {
            final ReadDistributionDto readDistributionDto = ReadDistributionDto.builder()
                    .occurrences((Integer) getValueFromRDataSet(rScript, "occurrences", i))
                    .reads((Integer) getValueFromRDataSet(rScript, "reads", i))
                    .fileName((String) getValueFromRDataSet(rScript, "file_name", i))
                    .build();

            readDistributionDtoList.add(readDistributionDto);
        }
        return readDistributionDtoList;
    }

    private RScript loadAllForRScript(final RScriptEnum rScriptEnum) {
        final RScript rScript = RScriptsConst.RScriptsMap.get(rScriptEnum);

        lock.lock();
        try {
            rService.evaluateRScript(rScriptEnum);
            rScript.getRVariablesMap().forEach((name, rVariable) -> rService.updateRVariableData(rVariable));
        } finally {
            lock.unlock();
        }

        return rScript;
    }

    private Object getValueFromRDataSet(final RScript rScript, final String variableName, final int index) {
        final List dataSet = rScript.getRVariablesMap().get(variableName).getRDataSet();
        return dataSet.size() > index ? dataSet.get(index) : null;
    }
}
