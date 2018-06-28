package com.nanoporeqc.csv.service;

import com.nanoporeqc.analysis.dto.ChartDto;
import com.nanoporeqc.analysis.dto.DuplicatedSequencesDto;
import com.nanoporeqc.analysis.dto.SequencesDistributionDto;
import com.nanoporeqc.analysis.dto.SummaryInfoDto;
import com.nanoporeqc.analysis.service.StatsService;
import com.nanoporeqc.exceptions.CsvDataCannotBeExportedException;
import com.nanoporeqc.r.consts.RDataConst;
import com.nanoporeqc.r.enumeration.RDataEnum;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {
    private final static String CSV_TYPE = "text/csv";
    private final static String CSV = ".csv";
    private static final String UTF_8 = "UTF-8";
    private static final String DEFAULT_SEPARATOR = ";";

    private StatsService statsService;

    public CsvService(final StatsService statsService) {
        this.statsService = statsService;
    }

    public void exportChartDataToCsv(final String name,
                                     final List<String> valuesNames,
                                     final HttpServletResponse response) {
        final ChartDto chartDto = statsService.getChartDataXY(name, valuesNames);
        setCsvAndDownloadHeader(name, response);
        try {
            final OutputStream outputStream = response.getOutputStream();
            outputStream.write(writeValues(chartDto, valuesNames).getBytes(UTF_8));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new CsvDataCannotBeExportedException();
        }
    }

    public void exportReadsToCsv(final RDataEnum rDataEnum,
                                 final HttpServletResponse response) {
        setCsvAndDownloadHeader(rDataEnum.getValue(), response);
        try {
            final OutputStream outputStream = response.getOutputStream();
            outputStream.write(writeValuesByRScript(rDataEnum).getBytes(UTF_8));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new CsvDataCannotBeExportedException();
        }
    }

    private void setCsvAndDownloadHeader(final String name, final HttpServletResponse response) {
        final String fileName = name + CSV;
        response.setContentType(CSV_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("FileName", fileName);
    }

    private String writeValues(final ChartDto chartDto,
                               final List<String> valuesNames) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final List<String> names = new ArrayList<>();
        names.addAll(valuesNames);
        writeLine(sb, names);
        for (int i = 0; i < chartDto.getValues().get(valuesNames.get(0)).size(); i++) {
            final Integer index = i;
            final List<String> values = new ArrayList<>();
            valuesNames.forEach(name -> values.add(String.valueOf(chartDto.getValues().get(name).get(index))));
            writeLine(sb, values);
        }
        return sb.toString();
    }

    private String writeValuesByRScript(final RDataEnum rDataEnum) throws IOException {
        switch (rDataEnum) {
            case SUMMARY_INFO:
                return writeSummaryInfoValues();
            case DUPLICATED_SEQUENCES:
                return writeDuplicatedReadsValues();
            case SEQUENCES_DISTRIBUTION:
                return writeReadDistributionValues();
            default:
                throw new CsvDataCannotBeExportedException();
        }
    }

    private String writeSummaryInfoValues() throws IOException {
        final List<SummaryInfoDto> data = statsService.getSummaryInformation();
        final StringBuilder sb = new StringBuilder();
        writeLine(sb, RDataConst.SUMMARY_INFO_DTO_VARIABLES);

        for (SummaryInfoDto aData : data) {
            final List<String> values = new ArrayList<>();
            values.add(String.valueOf(aData.getId()));
            values.add(aData.getFileName());
            values.add(String.valueOf(aData.getStrandIndexInChannel()));
            values.add(String.valueOf(aData.getChannelIndex()));
            values.add(String.valueOf(aData.getStartTime()));
            values.add(String.valueOf(aData.getDuration()));
            values.add(String.valueOf(aData.getEventsNo()));
            values.add(String.valueOf(aData.getHasTemplate()));
            values.add(String.valueOf(aData.getEventsNoTemplate()));
            values.add(aData.getHasComplement());
            values.add(String.valueOf(aData.getEventsNoComplement()));
            values.add(aData.getIs2d());

            writeLine(sb, values);
        }
        return sb.toString();
    }

    private String writeDuplicatedReadsValues() throws IOException {
        final List<DuplicatedSequencesDto> data = statsService.getDuplicatedSequences();
        final StringBuilder sb = new StringBuilder();
        writeLine(sb, RDataConst.DUPLICATED_SEQUENCES_DTO_VARIABLES);

        for (DuplicatedSequencesDto aData : data) {
            final List<String> values = new ArrayList<>();
            values.add(aData.getSequence());
            values.add(String.valueOf(aData.getCount()));
            writeLine(sb, values);
        }
        return sb.toString();
    }

    private String writeReadDistributionValues() throws IOException {
        final List<SequencesDistributionDto> data = statsService.getSequencesDistribution();
        final StringBuilder sb = new StringBuilder();
        writeLine(sb, RDataConst.SEQUENCES_DISTRIBUTION_DTO_VARIABLES);

        for (SequencesDistributionDto aData : data) {
            final List<String> values = new ArrayList<>();
            values.add(aData.getFileName());
            values.add(String.valueOf(aData.getOccurrences()));
            values.add(String.valueOf(aData.getReads()));
            writeLine(sb, values);
        }
        return sb.toString();
    }

    private void writeLine(StringBuilder sb, List<String> values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i));
            if (i != values.size() - 1) {
                sb.append(DEFAULT_SEPARATOR);
            }
        }
        sb.append("\n");
    }
}
