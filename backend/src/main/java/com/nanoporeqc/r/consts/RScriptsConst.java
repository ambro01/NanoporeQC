package com.nanoporeqc.r.consts;

import com.nanoporeqc.r.domain.RScript;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.r.enumeration.RVariableTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RScriptsConst {

    private static final RScript READ_INFO = new RScript(RScriptEnum.READ_INFO, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "file", new RVariable("file", RVariableTypeEnum.CHARACTER),
            "read", new RVariable("read", RVariableTypeEnum.NUMERIC),
            "channel", new RVariable("channel", RVariableTypeEnum.NUMERIC)));

    private static final RScript BASE_CALLED = new RScript(RScriptEnum.BASE_CALLED, Map.of(
            "id_full", new RVariable("id_full", RVariableTypeEnum.NUMERIC),
            "num_events_full", new RVariable("num_events_full", RVariableTypeEnum.NUMERIC),
            "duration_full", new RVariable("duration_full", RVariableTypeEnum.DOUBLE, 0),
            "start_time_full", new RVariable("start_time_full", RVariableTypeEnum.DOUBLE, 0),
            "strand_full", new RVariable("strand_full", RVariableTypeEnum.CHARACTER),
            "full_2D_full", new RVariable("full_2D_full", RVariableTypeEnum.LOGICAL)));

    private static final RScript EVENTS_COUNTS = new RScript(RScriptEnum.EVENTS_COUNTS, Map.of(
            "time", new RVariable("time", RVariableTypeEnum.DOUBLE, 0),
            "events", new RVariable("events", RVariableTypeEnum.NUMERIC)));

    private static final RScript EVENTS_DATA = new RScript(RScriptEnum.EVENTS_DATA, Map.of(
            "start_time", new RVariable("start_time", RVariableTypeEnum.DOUBLE, 0),
            "duration", new RVariable("duration", RVariableTypeEnum.DOUBLE, 0),
            "num_events", new RVariable("num_events", RVariableTypeEnum.NUMERIC)));

    private static final RScript READ_ACCUMULATION = new RScript(RScriptEnum.READ_ACCUMULATION, Map.of(
            "minute", new RVariable("minute", RVariableTypeEnum.DOUBLE, 0),
            "new_reads", new RVariable("new_reads", RVariableTypeEnum.NUMERIC),
            "accumulation", new RVariable("accumulation", RVariableTypeEnum.NUMERIC)));

    private static final RScript ACTIVE_CHANNELS = new RScript(RScriptEnum.ACTIVE_CHANNELS, Map.of(
            "minute", new RVariable("minute", RVariableTypeEnum.NUMERIC),
            "channels", new RVariable("channels", RVariableTypeEnum.NUMERIC)));

    private static final RScript BASE_PRODUCTION_RATE = new RScript(RScriptEnum.BASE_PRODUCTION_RATE, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "num_events", new RVariable("num_events", RVariableTypeEnum.NUMERIC),
            "duration", new RVariable("duration", RVariableTypeEnum.DOUBLE, 0),
            "start_time", new RVariable("start_time", RVariableTypeEnum.DOUBLE, 0),
            "strand", new RVariable("strand", RVariableTypeEnum.CHARACTER),
            "full_2D", new RVariable("full_2D", RVariableTypeEnum.LOGICAL),
            "bases_called", new RVariable("bases_called", RVariableTypeEnum.NUMERIC)));

    private static final RScript READ_CATEGORY_COUNTS = new RScript(RScriptEnum.READ_CATEGORY_COUNTS, Map.of(
            "category", new RVariable("category", RVariableTypeEnum.CHARACTER),
            "count", new RVariable("count", RVariableTypeEnum.NUMERIC),
            "files_count", new RVariable("files_count", RVariableTypeEnum.NUMERIC),
            "template_count", new RVariable("template_count", RVariableTypeEnum.NUMERIC),
            "complement_count", new RVariable("complement_count", RVariableTypeEnum.NUMERIC),
            "full_2d_count", new RVariable("full_2d_count", RVariableTypeEnum.NUMERIC)));

    private static final RScript READ_CATEGORY_QUALITY = new RScript(RScriptEnum.READ_CATEGORY_QUALITY, Map.of(
            "category", new RVariable("category", RVariableTypeEnum.CHARACTER),
            "min", new RVariable("min", RVariableTypeEnum.DOUBLE, 2),
            "max", new RVariable("max", RVariableTypeEnum.DOUBLE, 2),
            "mean", new RVariable("mean", RVariableTypeEnum.DOUBLE, 2),
            "median", new RVariable("median", RVariableTypeEnum.DOUBLE, 2)));

    private static final RScript READ_TYPE_PRODUCTION = new RScript(RScriptEnum.READ_TYPE_PRODUCTION, Map.of(
            "time_group", new RVariable("time_group", RVariableTypeEnum.DOUBLE),
            "full_2D", new RVariable("full_2D", RVariableTypeEnum.LOGICAL),
            "pass", new RVariable("pass", RVariableTypeEnum.LOGICAL),
            "count", new RVariable("count", RVariableTypeEnum.NUMERIC),
            "hour", new RVariable("hour", RVariableTypeEnum.DOUBLE)));

    private static final RScript READS_PER_CHANNEL = new RScript(RScriptEnum.READS_PER_CHANNEL, Map.of(
            "channel", new RVariable("channel", RVariableTypeEnum.NUMERIC),
            "reads", new RVariable("reads", RVariableTypeEnum.NUMERIC)));

    private static final RScript KB_PER_CHANNEL = new RScript(RScriptEnum.KB_PER_CHANNEL, Map.of(
            "channel", new RVariable("channel", RVariableTypeEnum.NUMERIC),
            "kb", new RVariable("kb", RVariableTypeEnum.NUMERIC)));

    private static final RScript BASE_CALLED_TEMPLATE = new RScript(RScriptEnum.BASE_CALLED_TEMPLATE, Map.of(
            "id_t", new RVariable("id_t", RVariableTypeEnum.NUMERIC),
            "num_events_t", new RVariable("num_events_t", RVariableTypeEnum.NUMERIC),
            "strand_t", new RVariable("strand_t", RVariableTypeEnum.CHARACTER),
            "full_2D_t", new RVariable("full_2D_t", RVariableTypeEnum.LOGICAL)));

    private static final RScript BASE_CALLED_COMPLEMENT = new RScript(RScriptEnum.BASE_CALLED_COMPLEMENT, Map.of(
            "id_c", new RVariable("id_c", RVariableTypeEnum.NUMERIC),
            "num_events_c", new RVariable("num_events_c", RVariableTypeEnum.NUMERIC),
            "strand_c", new RVariable("strand_c", RVariableTypeEnum.CHARACTER),
            "full_2D_c", new RVariable("full_2D_c", RVariableTypeEnum.LOGICAL)));

    private static final RScript READ_QUALITY = new RScript(RScriptEnum.READ_QUALITY, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "q_template", new RVariable("q_template", RVariableTypeEnum.DOUBLE, 2),
            "q_complement", new RVariable("q_complement", RVariableTypeEnum.DOUBLE, 2),
            "q_2D", new RVariable("q_2D", RVariableTypeEnum.DOUBLE, 2)));

    private static final RScript DUPLICATED_READS = new RScript(RScriptEnum.DUPLICATED_READS, Map.of(
            "sequence", new RVariable("sequence", RVariableTypeEnum.CHARACTER),
            "count", new RVariable("count", RVariableTypeEnum.NUMERIC)));

    private static final RScript NUCLEOTIDE_COUNTS = new RScript(RScriptEnum.NUCLEOTIDE_COUNTS, Map.of(
            "counts", new RVariable("counts", RVariableTypeEnum.NUMERIC),
            "A", new RVariable("A", RVariableTypeEnum.NUMERIC),
            "C", new RVariable("C", RVariableTypeEnum.NUMERIC),
            "G", new RVariable("G", RVariableTypeEnum.NUMERIC),
            "T", new RVariable("T", RVariableTypeEnum.NUMERIC),
            "N", new RVariable("N", RVariableTypeEnum.NUMERIC)));

    private static final RScript PER_CYCLE_BASE_CALL = new RScript(RScriptEnum.PER_CYCLE_BASE_CALL, Map.of(
            "cycle", new RVariable("cycle", RVariableTypeEnum.NUMERIC),
            "countA", new RVariable("countA", RVariableTypeEnum.NUMERIC),
            "countC", new RVariable("countC", RVariableTypeEnum.NUMERIC),
            "countG", new RVariable("countG", RVariableTypeEnum.NUMERIC),
            "countT", new RVariable("countT", RVariableTypeEnum.NUMERIC)));

    private static final RScript PER_CYCLE_QUALITY = new RScript(RScriptEnum.PER_CYCLE_QUALITY, Map.of(
            "cycle", new RVariable("cycle", RVariableTypeEnum.NUMERIC),
            "mean_", new RVariable("mean_", RVariableTypeEnum.DOUBLE, 2),
            "median_", new RVariable("median_", RVariableTypeEnum.DOUBLE, 2),
            "q25", new RVariable("q25", RVariableTypeEnum.DOUBLE, 2),
            "q50", new RVariable("q50", RVariableTypeEnum.DOUBLE, 2),
            "q75", new RVariable("q75", RVariableTypeEnum.DOUBLE, 2)));

    private static final RScript READ_QUALITY_SCORE = new RScript(RScriptEnum.READ_QUALITY_SCORE, Map.of(
            "quality", new RVariable("quality", RVariableTypeEnum.DOUBLE,2),
            "density", new RVariable("density", RVariableTypeEnum.DOUBLE, 2)));

    private static final RScript READ_DISTRIBUTION = new RScript(RScriptEnum.READ_DISTRIBUTION, Map.of(
            "occurrences", new RVariable("occurrences", RVariableTypeEnum.NUMERIC),
            "reads", new RVariable("reads", RVariableTypeEnum.NUMERIC),
            "file_name", new RVariable("file_name", RVariableTypeEnum.CHARACTER)));

    public static final Map<RScriptEnum, RScript> RScriptsMap = Map.ofEntries(
            Map.entry(RScriptEnum.READ_INFO, READ_INFO),
            Map.entry(RScriptEnum.BASE_CALLED, BASE_CALLED),
            Map.entry(RScriptEnum.EVENTS_COUNTS, EVENTS_COUNTS),
            Map.entry(RScriptEnum.READ_ACCUMULATION, READ_ACCUMULATION),
            Map.entry(RScriptEnum.ACTIVE_CHANNELS, ACTIVE_CHANNELS),
            Map.entry(RScriptEnum.BASE_PRODUCTION_RATE, BASE_PRODUCTION_RATE),
            Map.entry(RScriptEnum.READ_CATEGORY_COUNTS, READ_CATEGORY_COUNTS),
            Map.entry(RScriptEnum.READ_CATEGORY_QUALITY, READ_CATEGORY_QUALITY),
            Map.entry(RScriptEnum.READ_TYPE_PRODUCTION, READ_TYPE_PRODUCTION),
            Map.entry(RScriptEnum.KB_PER_CHANNEL, KB_PER_CHANNEL),
            Map.entry(RScriptEnum.READS_PER_CHANNEL, READS_PER_CHANNEL),
            Map.entry(RScriptEnum.EVENTS_DATA, EVENTS_DATA),
            Map.entry(RScriptEnum.BASE_CALLED_TEMPLATE, BASE_CALLED_TEMPLATE),
            Map.entry(RScriptEnum.BASE_CALLED_COMPLEMENT, BASE_CALLED_COMPLEMENT),
            Map.entry(RScriptEnum.READ_QUALITY, READ_QUALITY),
            Map.entry(RScriptEnum.DUPLICATED_READS, DUPLICATED_READS),
            Map.entry(RScriptEnum.NUCLEOTIDE_COUNTS, NUCLEOTIDE_COUNTS),
            Map.entry(RScriptEnum.PER_CYCLE_BASE_CALL, PER_CYCLE_BASE_CALL),
            Map.entry(RScriptEnum.READ_QUALITY_SCORE, READ_QUALITY_SCORE),
            Map.entry(RScriptEnum.READ_DISTRIBUTION, READ_DISTRIBUTION),
            Map.entry(RScriptEnum.PER_CYCLE_QUALITY, PER_CYCLE_QUALITY));

    public static final List<String> DUPLICATED_READS_VARIABLES = List.of("sequence", "coubt");

    public static final List<String> READ_DISTRIBUTION_VARIABLES = List.of("fileName", "occurrences", "reads");

    public static final List<String> SUMMARY_INFO_VARIABLES =  Stream.of(
            "id", "fileName", "strandIndexInChannel", "channelIndex", "startTime", "duration", "eventsNo", "hasTemplate",
            "eventsNoTemplate", "hasComplement", "eventsNoComplement", "is2d")
            .collect(Collectors.toList());

}