package com.nanoporeqc.r.enumeration;

public enum RScriptEnum {
    READ_INFO("readInfo.R"),
    BASE_CALLED("baseCalled.R"),
    EVENT_DATA("eventData.R"),
    READ_ACCUMULATION("readAccumulation.R"),
    ACTIVE_CHANNELS("activeChannels.R"),
    BASE_PRODUCTION_RATE("baseProductionRate.R"),
    READ_CATEGORY_COUNTS("readCategoryCounts.R"),
    READ_CATEGORY_QUALITY("readCategoryQuality.R"),
    READ_TYPE_PRODUCTION("readTypeProduction.R");

    private String fileName;

    RScriptEnum(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
