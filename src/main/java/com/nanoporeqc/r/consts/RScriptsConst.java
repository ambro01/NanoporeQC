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

    private static final RScript EVENT_DATA = new RScript(RScriptEnum.EVENT_DATA, Map.of(
            "id", new RVariable("id", RVariableTypeEnum.NUMERIC),
            "start_time", new RVariable("start_time", RVariableTypeEnum.DOUBLE),
            "duration", new RVariable("duration", RVariableTypeEnum.DOUBLE),
            "num_events", new RVariable("num_events", RVariableTypeEnum.NUMERIC),
            "median_signal", new RVariable("median_signal", RVariableTypeEnum.DOUBLE)));

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
            "count", new RVariable("count", RVariableTypeEnum.NUMERIC)));

    private static final RScript READ_CATEGORY_QUALITY = new RScript(RScriptEnum.READ_CATEGORY_QUALITY, Map.of(
            "category", new RVariable("category", RVariableTypeEnum.CHARACTER),
            "min", new RVariable("min", RVariableTypeEnum.NUMERIC),
            "max", new RVariable("max", RVariableTypeEnum.NUMERIC),
            "mean", new RVariable("mean", RVariableTypeEnum.NUMERIC),
            "median", new RVariable("median", RVariableTypeEnum.NUMERIC)));

    private static final RScript READ_TYPE_PRODUCTION = new RScript(RScriptEnum.READ_TYPE_PRODUCTION, Map.of(
            "time_group", new RVariable("time_group", RVariableTypeEnum.DOUBLE),
            "full_2D", new RVariable("full_2D", RVariableTypeEnum.LOGICAL),
            "pass", new RVariable("pass", RVariableTypeEnum.LOGICAL),
            "count", new RVariable("count", RVariableTypeEnum.NUMERIC),
            "hour", new RVariable("hour", RVariableTypeEnum.DOUBLE)));

    public static final Map<RScriptEnum, RScript> RScriptsMap = Map.of(
            RScriptEnum.READ_INFO, READ_INFO,
            RScriptEnum.BASE_CALLED, BASE_CALLED,
            RScriptEnum.EVENT_DATA, EVENT_DATA,
            RScriptEnum.READ_ACCUMULATION, READ_ACCUMULATION,
            RScriptEnum.ACTIVE_CHANNELS, ACTIVE_CHANNELS,
            RScriptEnum.BASE_PRODUCTION_RATE, BASE_PRODUCTION_RATE,
            RScriptEnum.READ_CATEGORY_COUNTS, READ_CATEGORY_COUNTS,
            RScriptEnum.READ_CATEGORY_QUALITY, READ_CATEGORY_QUALITY,
            RScriptEnum.READ_TYPE_PRODUCTION, READ_TYPE_PRODUCTION);
}