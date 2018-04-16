package com.nanoporeqc.r.consts;

import com.nanoporeqc.r.domain.RScript;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.enumeration.RScriptEnum;
import com.nanoporeqc.r.enumeration.RVariableTypeEnum;

import java.util.Map;

public class RScriptsConst {

    private static final RScript READ_INFO = new RScript(RScriptEnum.READ_INFO, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "file", new RVariable("file", RVariableTypeEnum.CHARACTER),
            "read", new RVariable("read", RVariableTypeEnum.NUMERIC),
            "channel", new RVariable("channel", RVariableTypeEnum.NUMERIC),
            "mux", new RVariable("mux", RVariableTypeEnum.NUMERIC),
            "pass", new RVariable("pass", RVariableTypeEnum.LOGICAL)));

    private static final RScript BASE_CALLED = new RScript(RScriptEnum.BASE_CALLED, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "num_events", new RVariable("num_events", RVariableTypeEnum.NUMERIC),
            "duration", new RVariable("duration", RVariableTypeEnum.DOUBLE),
            "start_time", new RVariable("start_time", RVariableTypeEnum.DOUBLE),
            "strand", new RVariable("strand", RVariableTypeEnum.CHARACTER),
            "full_2D", new RVariable("full_2D", RVariableTypeEnum.LOGICAL)));

    private static final RScript EVENT_COUNTS = new RScript(RScriptEnum.EVENT_COUNTS, Map.of(
            "time", new RVariable("time", RVariableTypeEnum.DOUBLE),
            "events", new RVariable("events", RVariableTypeEnum.NUMERIC)));

    private static final RScript READ_ACCUMULATION = new RScript(RScriptEnum.READ_ACCUMULATION, Map.of(
            "minute", new RVariable("minute", RVariableTypeEnum.DOUBLE),
            "new_reads", new RVariable("new_reads", RVariableTypeEnum.NUMERIC),
            "accumulation", new RVariable("accumulation", RVariableTypeEnum.NUMERIC)));

    private static final RScript ACTIVE_CHANNELS = new RScript(RScriptEnum.ACTIVE_CHANNELS, Map.of(
            "minute", new RVariable("minute", RVariableTypeEnum.NUMERIC),
            "channels", new RVariable("channels", RVariableTypeEnum.NUMERIC)));

    private static final RScript BASE_PRODUCTION_RATE = new RScript(RScriptEnum.BASE_PRODUCTION_RATE, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "num_events", new RVariable("num_events", RVariableTypeEnum.NUMERIC),
            "duration", new RVariable("duration", RVariableTypeEnum.DOUBLE),
            "start_time", new RVariable("start_time", RVariableTypeEnum.DOUBLE),
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
            "min", new RVariable("min", RVariableTypeEnum.DOUBLE),
            "max", new RVariable("max", RVariableTypeEnum.DOUBLE),
            "mean", new RVariable("mean", RVariableTypeEnum.DOUBLE),
            "median", new RVariable("median", RVariableTypeEnum.DOUBLE)));

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

    public static final Map<RScriptEnum, RScript> RScriptsMap = Map.ofEntries(
            Map.entry(RScriptEnum.READ_INFO, READ_INFO),
            Map.entry(RScriptEnum.BASE_CALLED, BASE_CALLED),
            Map.entry(RScriptEnum.EVENT_COUNTS, EVENT_COUNTS),
            Map.entry(RScriptEnum.READ_ACCUMULATION, READ_ACCUMULATION),
            Map.entry(RScriptEnum.ACTIVE_CHANNELS, ACTIVE_CHANNELS),
            Map.entry(RScriptEnum.BASE_PRODUCTION_RATE, BASE_PRODUCTION_RATE),
            Map.entry(RScriptEnum.READ_CATEGORY_COUNTS, READ_CATEGORY_COUNTS),
            Map.entry(RScriptEnum.READ_CATEGORY_QUALITY, READ_CATEGORY_QUALITY),
            Map.entry(RScriptEnum.READ_TYPE_PRODUCTION, READ_TYPE_PRODUCTION),
            Map.entry(RScriptEnum.KB_PER_CHANNEL, KB_PER_CHANNEL),
            Map.entry(RScriptEnum.READS_PER_CHANNEL, READS_PER_CHANNEL));
}