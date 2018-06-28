package com.nanoporeqc.analysis.service;

import com.nanoporeqc.analysis.dto.ChartDto;
import com.nanoporeqc.analysis.dto.DuplicatedSequencesDto;
import com.nanoporeqc.analysis.dto.SequencesDistributionDto;
import com.nanoporeqc.analysis.dto.SummaryInfoDto;
import com.nanoporeqc.r.consts.RDataConst;
import com.nanoporeqc.r.domain.RData;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.enumeration.RDataEnum;
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
        final RDataEnum rDataEnum = RDataEnum.getEnumByValue(name);
        final RData rData = RDataConst.RDataMap.get(rDataEnum);

        final List<RVariable> valuesList = valuesNames.stream()
                .map(yName -> rData.getRVariablesMap().get(yName))
                .collect(Collectors.toList());

        lock.lock();
        try {
            valuesList.forEach(rVariable -> rService.updateRVariableData(rDataEnum, rVariable));
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
        final RData readInfo = loadAllForRScript(RDataEnum.SUMMARY_INFO);
        final String template = "template";
        final String complement = "complement";

        final List<SummaryInfoDto> infoDtoList = new ArrayList<>();

        for (int i = 0; i < readInfo.getRVariablesMap().get("id").getRDataSet().size(); ++i) {
            final SummaryInfoDto infoDto = SummaryInfoDto.builder()
                    .id(i + 1)
                    .fileName((String) getValueFromRDataSet(readInfo, "file", i))
                    .channelIndex((Integer) getValueFromRDataSet(readInfo, "channel", i))
                    .strandIndexInChannel((Integer) getValueFromRDataSet(readInfo, "read", i))
                    .startTime((Double) getValueFromRDataSet(readInfo, "start_time", i))
                    .duration((Double) getValueFromRDataSet(readInfo, "duration", i))
                    .eventsNo((Integer) getValueFromRDataSet(readInfo, "num_events", i))
                    .hasTemplate(template.equals(String.valueOf(getValueFromRDataSet(readInfo, "strand_t", i))) ? "true" : "false")
                    .hasComplement(complement.equals(String.valueOf(getValueFromRDataSet(readInfo, "strand_c", i))) ? "true" : "false")
                    .eventsNoTemplate((Integer) getValueFromRDataSet(readInfo, "num_events_t", i))
                    .eventsNoComplement((Integer) getValueFromRDataSet(readInfo, "num_events_c", i))
                    .is2d(Optional.ofNullable(getValueFromRDataSet(readInfo, "full_2D_t", i))
                            .map(o -> (Integer) o).map(integer -> integer > 0).map(aBoolean -> aBoolean ? "true" : "false").orElse("false"))
                    .build();

            infoDtoList.add(infoDto);
        }
        return infoDtoList;
    }

    public List<DuplicatedSequencesDto> getDuplicatedSequences() {
        LOGGER.info("Getting duplicated reads");
        final RData rData = loadAllForRScript(RDataEnum.DUPLICATED_SEQUENCES);
        final List<DuplicatedSequencesDto> duplicatedSequencesDtoList = new ArrayList<>();

        for (int i = 0; i < rData.getRVariablesMap().get("count").getRDataSet().size(); ++i) {
            final DuplicatedSequencesDto duplicatedSequencesDto = DuplicatedSequencesDto.builder()
                    .count((Integer) getValueFromRDataSet(rData, "count", i))
                    .sequence((String) getValueFromRDataSet(rData, "sequence", i))
                    .build();

            duplicatedSequencesDtoList.add(duplicatedSequencesDto);
        }
        return duplicatedSequencesDtoList;
    }

    public List<SequencesDistributionDto> getSequencesDistribution() {
        LOGGER.info("Getting reads distribution");
        final RData rData = loadAllForRScript(RDataEnum.SEQUENCES_DISTRIBUTION);
        final List<SequencesDistributionDto> sequencesDistributionDtoList = new ArrayList<>();

        for (int i = 0; i < rData.getRVariablesMap().get("occurrences").getRDataSet().size(); ++i) {
            final SequencesDistributionDto sequencesDistributionDto = SequencesDistributionDto.builder()
                    .occurrences((Integer) getValueFromRDataSet(rData, "occurrences", i))
                    .reads((Integer) getValueFromRDataSet(rData, "reads", i))
                    .fileName((String) getValueFromRDataSet(rData, "fileName", i))
                    .build();

            sequencesDistributionDtoList.add(sequencesDistributionDto);
        }
        return sequencesDistributionDtoList;
    }

    private RData loadAllForRScript(final RDataEnum rDataEnum) {
        final RData rData = RDataConst.RDataMap.get(rDataEnum);
        lock.lock();
        try {
            rData.getRVariablesMap().forEach((name, rVariable) -> rService.updateRVariableData(rDataEnum, rVariable));
        } finally {
            lock.unlock();
        }

        return rData;
    }

    private Object getValueFromRDataSet(final RData rData, final String variableName, final int index) {
        final List dataSet = rData.getRVariablesMap().get(variableName).getRDataSet();
        return dataSet.size() > index ? dataSet.get(index) : null;
    }
}
