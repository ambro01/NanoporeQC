package com.nanoporeqc.r.enumeration;

import com.nanoporeqc.analysis.domain.Type;

import java.util.HashMap;
import java.util.Map;

public enum RDataEnum {
    NUCLEOTIDES_COUNTS("nucleotidesCounts", Type.FastQ),
    READS_QUALITY("readsQuality", Type.FastQ),
    READS_QUALITY_DENSITY("readsQualityDensity", Type.FastQ),
    BASES_QUALITY("basesQuality", Type.FastQ),
    BASES_QUALITY_DENSITY("basesQualityDensity", Type.FastQ),
    BASES_CALLS("basesCalls", Type.FastQ),
    BASES_CG_CONTENT("basesCgContent", Type.FastQ),
    SEQUENCES_DISTRIBUTION("sequencesDistribution", Type.FastQ),
    DUPLICATED_SEQUENCES("duplicatedSequences", Type.FastQ),

    SUMMARY_INFO("summaryInfo", Type.Fast5),
    EVENTS_COUNTS("eventsCounts", Type.Fast5),
    READS_ACCUMULATION("readsAccumulation", Type.Fast5),
    ACTIVE_CHANNELS("activeChannels", Type.Fast5),
    READS_CATEGORY_COUNTS("readsCategoryCounts", Type.Fast5),
    READS_CATEGORY_QUALITY("readsCategoryQuality", Type.Fast5),
    READS_PER_CHANNEL("readsPerChannel", Type.Fast5),
    KB_PER_CHANNEL("kbPerChannel", Type.Fast5),
    READS_QUALITY_MULTI("readsQualityMulti", Type.Fast5),
    READS_QUALITY_DENSITY_MULTI("readsQualityDensityMulti", Type.Fast5),
    EVENTS_DATA("eventsData", Type.Fast5),
    BASE_CALLED("baseCalled", Type.Fast5),
    BASE_CALLED_TEMPLATE("baseCalledTemplate", Type.Fast5),
    BASE_CALLED_COMPLEMENT("baseCalledComplement", Type.Fast5);

    private String value;
    private Type type;

    private static final Map<String, RDataEnum> map = new HashMap<>();

    static {
        for (RDataEnum myEnum : values()) {
            map.put(myEnum.getValue(), myEnum);
        }
    }

    public static RDataEnum getEnumByValue(final String value) {
        return map.get(value);
    }

    RDataEnum(final String value, final Type type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }


}
