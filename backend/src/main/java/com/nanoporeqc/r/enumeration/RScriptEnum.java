package com.nanoporeqc.r.enumeration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public enum RScriptEnum {
    READ_INFO("readInfo"),
    BASE_CALLED("baseCalled"),
    EVENTS_COUNTS("eventsCounts"),
    READ_ACCUMULATION("readAccumulation"),
    ACTIVE_CHANNELS("activeChannels"),
    BASE_PRODUCTION_RATE("baseProductionRate"),
    READ_CATEGORY_COUNTS("readCategoryCounts"),
    READ_CATEGORY_QUALITY("readCategoryQuality"),
    READ_TYPE_PRODUCTION("readTypeProduction"),
    READS_PER_CHANNEL("readsPerChannel"),
    KB_PER_CHANNEL("kbPerChannel"),
    EVENTS_DATA("eventsData"),
    BASE_CALLED_TEMPLATE("baseCalledTemplate"),
    BASE_CALLED_COMPLEMENT("baseCalledComplement"),
    READ_QUALITY("readQuality"),
    READ_QUALITY_DENSITY("readQualityDensity"),
    LOAD_LIBRARIES("loadLibraries"),
    SAVE_SUMMARY_FAST5("saveSummaryFast5"),
    READ_SUMMARY_FAST5("readSummaryFast5"),
    COMMON("common"),
    ALL_FUNCTIONS("allFunctions"),
    READ_FAST5_SUMMARY_FROM_DIR("readFast5SummaryFromDir"),
    READ_FASTQ_SUMMARY_FROM_DIR("readFastQSummaryFromDir"),
    DUPLICATED_READS("duplicatedReads"),
    NUCLEOTIDE_COUNTS("nucleotideCounts"),
    PER_CYCLE_BASE_CALL("perCycleBaseCall"),
    PER_CYCLE_QUALITY("perCycleQuality"),
    PER_CYCLE_CG_CONTENT("perCycleCGContent"),
    READ_QUALITY_FASTQ("readQualityFastQ"),
    READ_QUALITY_FASTQ_FROM_FAST5("readQualityFastQFromFast5"),
    READ_QUALITY_DENSITY_FASTQ("readQualityDensityFastQ"),
    READ_QUALITY_DENSITY_FASTQ_FROM_FAST5("readQualityDensityFastQFromFast5"),
    READ_DISTRIBUTION("readDistribution"),
    READ_FASTQ_SUMMARY_FROM_FAST5_SUMMARY("readFastQSummaryFromFast5Summary"),
    READ_SUMMARY_FASTQ("readSummaryFastQ"),
    SAVE_SUMMARY_FASTQ("saveSummaryFastQ"),
    SAVE_QUALITY_SUMMARY("saveQualitySummary"),
    READ_QUALITY_SUMMARY("readQualitySummary");

    private String value;

    private static final Map<String, RScriptEnum> map = new HashMap<>();

    public static final Set<RScriptEnum> INITIALS_SCRIPTS = new LinkedHashSet<>(Arrays.asList(LOAD_LIBRARIES, COMMON, ALL_FUNCTIONS));

    static {
        for (RScriptEnum myEnum : values()) {
            map.put(myEnum.getValue(), myEnum);
        }
    }

    public static RScriptEnum getEnumByValue(final String value){
        return map.get(value);
    }

    RScriptEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getFileName() {
        return value + ".R";
    }
}
