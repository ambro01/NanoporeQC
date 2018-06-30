package com.nanoporeqc.r.consts;

import com.nanoporeqc.r.domain.RData;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.enumeration.RDataEnum;
import com.nanoporeqc.r.enumeration.RVariableTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RDataConst {

    public static final List<String> DUPLICATED_SEQUENCES_DTO_VARIABLES = List.of("sequence", "coubt");

    public static final List<String> SEQUENCES_DISTRIBUTION_DTO_VARIABLES = List.of("fileName", "occurrences", "reads");

    public static final List<String> SUMMARY_INFO_DTO_VARIABLES = Stream.of(
            "id", "fileName", "strandIndexInChannel", "channelIndex", "startTime", "duration", "eventsNo", "hasTemplate",
            "eventsNoTemplate", "hasComplement", "eventsNoComplement", "is2d")
            .collect(Collectors.toList());

    private static final RData NUCLEOTIDES_COUNTS = new RData(RDataEnum.NUCLEOTIDES_COUNTS, Map.of(
            "counts", new RVariable("counts", RVariableTypeEnum.NUMERIC),
            "A", new RVariable("A", RVariableTypeEnum.NUMERIC),
            "C", new RVariable("C", RVariableTypeEnum.NUMERIC),
            "G", new RVariable("G", RVariableTypeEnum.NUMERIC),
            "T", new RVariable("T", RVariableTypeEnum.NUMERIC),
            "N", new RVariable("N", RVariableTypeEnum.NUMERIC)));

    private static final RData READS_QUALITY = new RData(RDataEnum.READS_QUALITY, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "quality", new RVariable("quality", RVariableTypeEnum.DOUBLE, 2),
            "mean", new RVariable("mean", RVariableTypeEnum.DOUBLE, 2),
            "median", new RVariable("median", RVariableTypeEnum.DOUBLE, 2),
            "q25", new RVariable("q25", RVariableTypeEnum.DOUBLE, 2),
            "q50", new RVariable("q50", RVariableTypeEnum.DOUBLE, 2),
            "q75", new RVariable("q75", RVariableTypeEnum.DOUBLE, 2)));

    private static final RData READS_QUALITY_DENSITY = new RData(RDataEnum.READS_QUALITY_DENSITY, Map.of(
            "quality", new RVariable("quality", RVariableTypeEnum.DOUBLE, 2),
            "density", new RVariable("density", RVariableTypeEnum.DOUBLE, 2)));

    private static final RData BASES_QUALITY = new RData(RDataEnum.BASES_QUALITY, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "quality", new RVariable("quality", RVariableTypeEnum.DOUBLE, 2),
            "mean", new RVariable("mean", RVariableTypeEnum.DOUBLE, 2),
            "median", new RVariable("median", RVariableTypeEnum.DOUBLE, 2),
            "q25", new RVariable("q25", RVariableTypeEnum.DOUBLE, 2),
            "q50", new RVariable("q50", RVariableTypeEnum.DOUBLE, 2),
            "q75", new RVariable("q75", RVariableTypeEnum.DOUBLE, 2)));

    private static final RData BASES_QUALITY_DENSITY = new RData(RDataEnum.BASES_QUALITY_DENSITY, Map.of(
            "quality", new RVariable("quality", RVariableTypeEnum.DOUBLE, 2),
            "density", new RVariable("density", RVariableTypeEnum.DOUBLE, 2)));

    private static final RData BASES_CALLS = new RData(RDataEnum.BASES_CALLS, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "A", new RVariable("A", RVariableTypeEnum.NUMERIC),
            "C", new RVariable("C", RVariableTypeEnum.NUMERIC),
            "G", new RVariable("G", RVariableTypeEnum.NUMERIC),
            "T", new RVariable("T", RVariableTypeEnum.NUMERIC),
            "N", new RVariable("N", RVariableTypeEnum.NUMERIC)));

    private static final RData BASES_CG_CONTENT = new RData(RDataEnum.BASES_CG_CONTENT, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "cgContent", new RVariable("cgContent", RVariableTypeEnum.DOUBLE, 2)));

    private static final RData BASES_CG_DENSITY = new RData(RDataEnum.BASES_CG_DENSITY, Map.of(
            "cgContent", new RVariable("cgContent", RVariableTypeEnum.DOUBLE, 2),
            "density", new RVariable("density", RVariableTypeEnum.DOUBLE, 2)));

    private static final RData SEQUENCES_DISTRIBUTION = new RData(RDataEnum.SEQUENCES_DISTRIBUTION, Map.of(
            "occurrences", new RVariable("occurrences", RVariableTypeEnum.NUMERIC),
            "reads", new RVariable("reads", RVariableTypeEnum.NUMERIC),
            "fileName", new RVariable("fileName", RVariableTypeEnum.CHARACTER)));

    private static final RData DUPLICATED_SEQUENCES = new RData(RDataEnum.DUPLICATED_SEQUENCES, Map.of(
            "sequence", new RVariable("sequence", RVariableTypeEnum.CHARACTER),
            "count", new RVariable("count", RVariableTypeEnum.NUMERIC)));


    private static final RData SUMMARY_INFO = new RData(RDataEnum.SUMMARY_INFO, Map.ofEntries(
            Map.entry("id", new RVariable("id", RVariableTypeEnum.NUMERIC)),
            Map.entry("file", new RVariable("file", RVariableTypeEnum.CHARACTER)),
            Map.entry("read", new RVariable("read", RVariableTypeEnum.NUMERIC)),
            Map.entry("channel", new RVariable("channel", RVariableTypeEnum.NUMERIC)),
            Map.entry("start_time", new RVariable("start_time", RVariableTypeEnum.DOUBLE, 0)),
            Map.entry("duration", new RVariable("duration", RVariableTypeEnum.DOUBLE, 0)),
            Map.entry("num_events", new RVariable("num_events", RVariableTypeEnum.NUMERIC)),
            Map.entry("num_events_t", new RVariable("num_events_t", RVariableTypeEnum.NUMERIC)),
            Map.entry("strand_t", new RVariable("strand_t", RVariableTypeEnum.CHARACTER)),
            Map.entry("full_2D_t", new RVariable("full_2D_t", RVariableTypeEnum.LOGICAL)),
            Map.entry("num_events_c", new RVariable("num_events_c", RVariableTypeEnum.NUMERIC)),
            Map.entry("strand_c", new RVariable("strand_c", RVariableTypeEnum.CHARACTER)),
            Map.entry("full_2D_c", new RVariable("full_2D_c", RVariableTypeEnum.LOGICAL))));

    private static final RData EVENTS_COUNTS = new RData(RDataEnum.EVENTS_COUNTS, Map.of(
            "time", new RVariable("time", RVariableTypeEnum.DOUBLE, 0),
            "events", new RVariable("events", RVariableTypeEnum.NUMERIC)));

    private static final RData READS_ACCUMULATION = new RData(RDataEnum.READS_ACCUMULATION, Map.of(
            "minute", new RVariable("minute", RVariableTypeEnum.DOUBLE, 0),
            "newReads", new RVariable("newReads", RVariableTypeEnum.NUMERIC),
            "accumulation", new RVariable("accumulation", RVariableTypeEnum.NUMERIC)));

    private static final RData ACTIVE_CHANNELS = new RData(RDataEnum.ACTIVE_CHANNELS, Map.of(
            "minute", new RVariable("minute", RVariableTypeEnum.NUMERIC),
            "channels", new RVariable("channels", RVariableTypeEnum.NUMERIC)));

    private static final RData READS_CATEGORY_COUNTS = new RData(RDataEnum.READS_CATEGORY_COUNTS, Map.of(
            "category", new RVariable("category", RVariableTypeEnum.CHARACTER),
            "count", new RVariable("count", RVariableTypeEnum.NUMERIC),
            "files_count", new RVariable("files_count", RVariableTypeEnum.NUMERIC),
            "template_count", new RVariable("template_count", RVariableTypeEnum.NUMERIC),
            "complement_count", new RVariable("complement_count", RVariableTypeEnum.NUMERIC),
            "full_2d_count", new RVariable("full_2d_count", RVariableTypeEnum.NUMERIC)));

    private static final RData READS_CATEGORY_QUALITY = new RData(RDataEnum.READS_CATEGORY_QUALITY, Map.of(
            "category", new RVariable("category", RVariableTypeEnum.CHARACTER),
            "min", new RVariable("min", RVariableTypeEnum.DOUBLE, 2),
            "max", new RVariable("max", RVariableTypeEnum.DOUBLE, 2),
            "mean", new RVariable("mean", RVariableTypeEnum.DOUBLE, 2),
            "median", new RVariable("median", RVariableTypeEnum.DOUBLE, 2)));

    private static final RData READS_PER_CHANNEL = new RData(RDataEnum.READS_PER_CHANNEL, Map.of(
            "channel", new RVariable("channel", RVariableTypeEnum.NUMERIC),
            "reads", new RVariable("reads", RVariableTypeEnum.NUMERIC)));

    private static final RData KB_PER_CHANNEL = new RData(RDataEnum.KB_PER_CHANNEL, Map.of(
            "channel", new RVariable("channel", RVariableTypeEnum.NUMERIC),
            "kb", new RVariable("kb", RVariableTypeEnum.NUMERIC)));

    private static final RData READS_QUALITY_MULTI = new RData(RDataEnum.READS_QUALITY_MULTI, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "q_template", new RVariable("q_template", RVariableTypeEnum.DOUBLE, 2),
            "q_complement", new RVariable("q_complement", RVariableTypeEnum.DOUBLE, 2),
            "q_2D", new RVariable("q_2D", RVariableTypeEnum.DOUBLE, 2)));

    private static final RData READS_QUALITY_DENSITY_MULTI = new RData(RDataEnum.READS_QUALITY_DENSITY_MULTI, Map.of(
            "quality_template", new RVariable("quality_template", RVariableTypeEnum.DOUBLE, 2),
            "quality_complement", new RVariable("quality_complement", RVariableTypeEnum.DOUBLE, 2),
            "quality_2D", new RVariable("quality_2D", RVariableTypeEnum.DOUBLE, 2),
            "density_template", new RVariable("density_template", RVariableTypeEnum.DOUBLE, 2),
            "density_complement", new RVariable("density_complement", RVariableTypeEnum.DOUBLE, 2),
            "density_2D", new RVariable("density_2D", RVariableTypeEnum.DOUBLE, 2)));

    private static final Map<String, RVariable> OUTLIERS_VARIABLES = Map.of(
            "outliersCount", new RVariable("outliersCount", RVariableTypeEnum.NUMERIC),
            "total", new RVariable("total", RVariableTypeEnum.NUMERIC),
            "proportion", new RVariable("proportion", RVariableTypeEnum.DOUBLE, 2),
            "outliersMean", new RVariable("outliersMean", RVariableTypeEnum.DOUBLE, 2),
            "meanWithOutliers", new RVariable("meanWithOutliers", RVariableTypeEnum.DOUBLE, 2),
            "meanWithoutOutliers", new RVariable("meanWithoutOutliers", RVariableTypeEnum.DOUBLE, 2),
            "outliersId", new RVariable("outliersId", RVariableTypeEnum.NUMERIC),
            "outliersValues", new RVariable("outliersValues", RVariableTypeEnum.DOUBLE, 2),
            "notOutliersId", new RVariable("notOutliersId", RVariableTypeEnum.NUMERIC),
            "notOutliersValues", new RVariable("notOutliersValues", RVariableTypeEnum.DOUBLE, 2));

    private static final RData BASE_OUTLIERS_QUALITY = new RData(RDataEnum.BASE_OUTLIERS_QUALITY, OUTLIERS_VARIABLES);
    private static final RData READS_OUTLIERS_QUALITY = new RData(RDataEnum.READS_OUTLIERS_QUALITY, OUTLIERS_VARIABLES);
    private static final RData READS_2D_OUTLIERS_QUALITY = new RData(RDataEnum.READS_2D_OUTLIERS_QUALITY, OUTLIERS_VARIABLES);
    private static final RData READS_TEMPLATE_OUTLIERS_QUALITY = new RData(RDataEnum.READS_TEMPLATE_OUTLIERS_QUALITY, OUTLIERS_VARIABLES);
    private static final RData READS_COMPLEMENT_OUTLIERS_QUALITY = new RData(RDataEnum.READS_COMPLEMENT_OUTLIERS_QUALITY, OUTLIERS_VARIABLES);

    public static final Map<RDataEnum, RData> RDataMap = Map.ofEntries(
            Map.entry(RDataEnum.NUCLEOTIDES_COUNTS, NUCLEOTIDES_COUNTS),
            Map.entry(RDataEnum.READS_QUALITY, READS_QUALITY),
            Map.entry(RDataEnum.READS_QUALITY_DENSITY, READS_QUALITY_DENSITY),
            Map.entry(RDataEnum.BASES_QUALITY, BASES_QUALITY),
            Map.entry(RDataEnum.BASES_QUALITY_DENSITY, BASES_QUALITY_DENSITY),
            Map.entry(RDataEnum.BASES_CALLS, BASES_CALLS),
            Map.entry(RDataEnum.BASES_CG_CONTENT, BASES_CG_CONTENT),
            Map.entry(RDataEnum.BASES_CG_DENSITY, BASES_CG_DENSITY),
            Map.entry(RDataEnum.SEQUENCES_DISTRIBUTION, SEQUENCES_DISTRIBUTION),
            Map.entry(RDataEnum.DUPLICATED_SEQUENCES, DUPLICATED_SEQUENCES),
            Map.entry(RDataEnum.SUMMARY_INFO, SUMMARY_INFO),
            Map.entry(RDataEnum.EVENTS_COUNTS, EVENTS_COUNTS),
            Map.entry(RDataEnum.READS_ACCUMULATION, READS_ACCUMULATION),
            Map.entry(RDataEnum.ACTIVE_CHANNELS, ACTIVE_CHANNELS),
            Map.entry(RDataEnum.READS_CATEGORY_COUNTS, READS_CATEGORY_COUNTS),
            Map.entry(RDataEnum.READS_CATEGORY_QUALITY, READS_CATEGORY_QUALITY),
            Map.entry(RDataEnum.KB_PER_CHANNEL, KB_PER_CHANNEL),
            Map.entry(RDataEnum.READS_PER_CHANNEL, READS_PER_CHANNEL),
            Map.entry(RDataEnum.READS_QUALITY_MULTI, READS_QUALITY_MULTI),
            Map.entry(RDataEnum.READS_QUALITY_DENSITY_MULTI, READS_QUALITY_DENSITY_MULTI),
            Map.entry(RDataEnum.BASE_OUTLIERS_QUALITY, BASE_OUTLIERS_QUALITY),
            Map.entry(RDataEnum.READS_OUTLIERS_QUALITY, READS_OUTLIERS_QUALITY),
            Map.entry(RDataEnum.READS_2D_OUTLIERS_QUALITY, READS_2D_OUTLIERS_QUALITY),
            Map.entry(RDataEnum.READS_TEMPLATE_OUTLIERS_QUALITY, READS_TEMPLATE_OUTLIERS_QUALITY),
            Map.entry(RDataEnum.READS_COMPLEMENT_OUTLIERS_QUALITY, READS_COMPLEMENT_OUTLIERS_QUALITY));

}