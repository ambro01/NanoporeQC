package com.nanoporeqc.analysis.service;

import com.nanoporeqc.analysis.dto.ChartDto;
import com.nanoporeqc.analysis.dto.DuplicatedSequenceDto;
import com.nanoporeqc.analysis.dto.ReadDistributionDto;
import com.nanoporeqc.analysis.dto.SummaryInfoDto;
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

    public ChartDto getChartDataXY(final String name, final List<String> valuesNames) {
        LOGGER.info("Getting chart of: " + name);
        final RScriptEnum rScriptEnum = RScriptEnum.getEnumByValue(name);
        final RScript rScript = RScriptsConst.RScriptsMap.get(rScriptEnum);

        final List<RVariable> valuesList = valuesNames.stream()
                .map(yName -> rScript.getRVariablesMap().get(yName))
                .collect(Collectors.toList());

        lock.lock();
        try {
            rService.evaluateRScript(rScriptEnum);
            valuesList.forEach(rService::updateRVariableData);
        } finally {
            lock.unlock();
        }

        return ChartDto.builder()
                .values(valuesList.stream()
                        .collect(Collectors.toMap(RVariable::getName, RVariable::getRDataSet)))
                .build();
    }

    public List<SummaryInfoDto> getSummaryInformation() {
        LOGGER.info("Getting summary info");
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
        LOGGER.info("Getting duplicated reads");
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
        LOGGER.info("Getting reads distribution");
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
