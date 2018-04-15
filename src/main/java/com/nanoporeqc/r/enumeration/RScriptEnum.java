package com.nanoporeqc.r.enumeration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public enum RScriptEnum {
    READ_INFO("readInfo"),
    BASE_CALLED("baseCalled"),
    EVENT_DATA("eventData"),
    READ_ACCUMULATION("readAccumulation"),
    ACTIVE_CHANNELS("activeChannels"),
    BASE_PRODUCTION_RATE("baseProductionRate"),
    READ_CATEGORY_COUNTS("readCategoryCounts"),
    READ_CATEGORY_QUALITY("readCategoryQuality"),
    READ_TYPE_PRODUCTION("readTypeProduction"),
    LOAD_LIBRARIES("loadLibraries"),
    SAVE_SUMMARY("saveSummary"),
    COMMON("common"),
    ALL_FUNCTIONS("allFunctions"),
    READ_FAST5_SUMMARY_FROM_DIR("readFast5SummaryFromDir");

    private String value;

    private static final Map<String, RScriptEnum> map = new HashMap<>();

    public static final Set<RScriptEnum> INITIALS_SCRIPTS = new LinkedHashSet<>(Arrays.asList(LOAD_LIBRARIES, COMMON, ALL_FUNCTIONS));

    static {
        for (RScriptEnum myEnum : values()) {
            map.put(myEnum.getValue(), myEnum);
        }
    }

    public static RScriptEnum getEnumByValue(String value){
        return map.get(value);
    }

    RScriptEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getFileName() {
        return value + ".R";
    }
}
