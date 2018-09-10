package com.nanoporeqc.csv.service;

import com.nanoporeqc.analysis.domain.ClusteringAlgorithm;
import com.nanoporeqc.analysis.dto.ChartDto;
import com.nanoporeqc.analysis.dto.ClusteringReadsDto;
import com.nanoporeqc.analysis.dto.D2DetectionDto;
import com.nanoporeqc.analysis.dto.DuplicatedSequencesDto;
import com.nanoporeqc.analysis.dto.OutlierDto;
import com.nanoporeqc.analysis.dto.ReadsInfoDto;
import com.nanoporeqc.analysis.dto.SequencesDistributionDto;
import com.nanoporeqc.analysis.dto.SummaryInfoDto;
import com.nanoporeqc.analysis.service.StatsService;
import com.nanoporeqc.csv.utils.CsvUtils;
import com.nanoporeqc.exceptions.CsvDataCannotBeExportedException;
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

    public void exportClusteringData(final ClusteringAlgorithm algorithm,
                                     final Integer clusterNumber,
                                     final HttpServletResponse response) {
        switch (algorithm) {
            case kmeans:
                exportReadsToCsv(RDataEnum.KMEANS_CLUSTERING, response);
            case mclust:
                if (clusterNumber > 0) {
                    exportReadsToCsv(RDataEnum.MCLUST_CLUSTERING, response);
                } else {
                    exportReadsToCsv(RDataEnum.MCLUST_CLUSTERING_WITHOUT_OUTLIERS, response);
                }
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

    private String writeValues(final ChartDto chartDto, final List<String> valuesNames) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final List<String> names = new ArrayList<>();
        names.addAll(valuesNames);
        writeLine(sb, names);
        for (int i = 0; i < chartDto.getValues().get(valuesNames.get(0)).size(); i++) {
            final Integer index = i;
            final List<String> values = new ArrayList<>();
            valuesNames.forEach(
                    name -> values.add(String.valueOf(chartDto.getValues().get(name).get(index)))
            );
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
            case READS_INFO:
                return writeReadsInfo();
            case KMEANS_CLUSTERING:
                return writeReadsClustering(rDataEnum);
            case MCLUST_CLUSTERING:
                return writeReadsClustering(rDataEnum);
            case MCLUST_CLUSTERING_WITHOUT_OUTLIERS:
                return writeReadsClustering(rDataEnum);
            case D2_DETECTION:
                return writeD2Detection();
            case OUTLIERS_DETECTION:
                return writeOutliersDetection();
            default:
                throw new CsvDataCannotBeExportedException();
        }
    }

    private String writeSummaryInfoValues() throws IOException {
        final List<SummaryInfoDto> data = statsService.getSummaryInformation();
        final StringBuilder sb = new StringBuilder();
        writeLine(sb, CsvUtils.getSetOfVariables(RDataEnum.SUMMARY_INFO));

        for (final SummaryInfoDto aData : data) {
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
        writeLine(sb, CsvUtils.getSetOfVariables(RDataEnum.DUPLICATED_SEQUENCES));

        for (final DuplicatedSequencesDto aData : data) {
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
        writeLine(sb, CsvUtils.getSetOfVariables(RDataEnum.SEQUENCES_DISTRIBUTION));

        for (final SequencesDistributionDto aData : data) {
            final List<String> values = new ArrayList<>();
            values.add(aData.getFileName());
            values.add(String.valueOf(aData.getOccurrences()));
            values.add(String.valueOf(aData.getReads()));
            writeLine(sb, values);
        }
        return sb.toString();
    }

    private String writeReadsInfo() throws IOException {
        final List<ReadsInfoDto> data = statsService.getReadsInfo();
        final StringBuilder sb = new StringBuilder();
        writeLine(sb, CsvUtils.getSetOfVariables(RDataEnum.READS_INFO));

        for (final ReadsInfoDto aData : data) {
            final List<String> values = new ArrayList<>();
            values.add(aData.getName());
            values.add(String.valueOf(aData.getId()));
            values.add(String.valueOf(aData.getLength()));
            values.add(String.valueOf(aData.getMode()));
            values.add(String.valueOf(aData.getMean()));
            values.add(String.valueOf(aData.getMedian()));
            values.add(String.valueOf(aData.getQuantile25()));
            values.add(String.valueOf(aData.getQuantile75()));
            values.add(String.valueOf(aData.getCgContent()));
            writeLine(sb, values);
        }
        return sb.toString();
    }

    private String writeReadsClustering(final RDataEnum rDataEnum) throws IOException {
        final List<ClusteringReadsDto> data = statsService.getClustering(rDataEnum);
        final StringBuilder sb = new StringBuilder();
        writeLine(sb, CsvUtils.getSetOfVariables(rDataEnum));

        for (final ClusteringReadsDto aData : data) {
            final List<String> values = new ArrayList<>();
            values.add(String.valueOf(aData.getCluster()));
            values.add(aData.getReadIds());
            writeLine(sb, values);
        }
        return sb.toString();
    }

    private String writeD2Detection() throws IOException {
        final List<D2DetectionDto> data = statsService.getD2Detection();
        final StringBuilder sb = new StringBuilder();
        writeLine(sb, CsvUtils.getSetOfVariables(RDataEnum.D2_DETECTION));

        for (D2DetectionDto aData : data) {
            final List<String> values = new ArrayList<>();
            values.add(aData.getClusterName());
            values.add(String.valueOf(aData.getMode()));
            values.add(aData.getReadIds());
            writeLine(sb, values);
        }
        return sb.toString();
    }

    private String writeOutliersDetection() throws IOException {
        final List<OutlierDto> data = statsService.getOutliersDetection();
        final StringBuilder sb = new StringBuilder();
        writeLine(sb, CsvUtils.getSetOfVariables(RDataEnum.OUTLIERS_DETECTION));

        for (final OutlierDto aData : data) {
            final List<String> values = new ArrayList<>();
            values.add(String.valueOf(aData.getOutlierPlace()));
            values.add(aData.getName());
            values.add(String.valueOf(aData.getReadId()));
            values.add(String.valueOf(aData.getDistanceFromCentre()));
            writeLine(sb, values);
        }
        return sb.toString();
    }

    private void writeLine(final StringBuilder sb, final List<String> values) throws IOException {
        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i));
            if (i != values.size() - 1) {
                sb.append(DEFAULT_SEPARATOR);
            }
        }
        sb.append("\n");
    }
}
