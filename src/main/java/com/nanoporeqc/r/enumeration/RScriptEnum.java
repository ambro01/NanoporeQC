package com.nanoporeqc.r.enumeration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum RScriptEnum {
    READ_INFO("readInfo"),
    BASE_CALLED("baseCalled"),
    EVENT_DATA("eventData"),
    READ_ACCUMULATION("readAccumulation"),
    ACTIVE_CHANNELS("activeChannels"),
    BASE_PRODUCTION_RATE("baseProductionRate"),
    READ_CATEGORY_COUNTS("readCategoryCounts"),
    READ_CATEGORY_QUALITY("readCategoryQuality"),
    READ_TYPE_PRODUCTION("readTypeProduction");

    private String value;

    private static final Map<String, RScriptEnum> map = new HashMap<>();

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
