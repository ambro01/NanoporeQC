package com.nanoporeqc.r.enumeration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public enum RScriptEnum {
    LOAD_LIBRARIES("loadLibraries"),
    COMMON("common"),
    ALL_FUNCTIONS("allFunctions"),
    SAVE_SUMMARY_FAST5("saveSummaryFast5"),
    READ_SUMMARY_FAST5("readSummaryFast5"),
    GENERATE_DATA_FAST5("generateDataFast5"),
    READ_FAST5_SUMMARY_FROM_DIR("readFast5SummaryFromDir"),
    READ_SUMMARY_FASTQ("readSummaryFastQ"),
    SAVE_SUMMARY_FASTQ("saveSummaryFastQ"),
    GENERATE_DATA_FASTQ("generateDataFastQ"),
    READ_FASTQ_SUMMARY_FROM_DIR("readFastQSummaryFromDir");

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
