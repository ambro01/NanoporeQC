package com.nanoporeqc.r.enumeration;

import com.nanoporeqc.analysis.domain.Type;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public enum RDataEnum {
    NUCLEOTIDES_COUNTS("nucleotidesCounts", Type.FastQ),
    READS_QUALITY("readsQuality", Type.FastQ),
    READS_QUALITY_DENSITY("readsQualityDensity", Type.FastQ),
    BASES_QUALITY("basesQuality", Type.FastQ),
    BASES_QUALITY_DENSITY("basesQualityDensity", Type.FastQ),
    BASES_CALLS("basesCalls", Type.FastQ),
    BASES_CG_CONTENT("basesCgContent", Type.FastQ),
    BASES_CG_DENSITY("basesCgDensity", Type.FastQ),
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
    BASE_CALLED_COMPLEMENT("baseCalledComplement", Type.Fast5),

    BASE_OUTLIERS_QUALITY("basesQualityOutliers", Type.FastQ, TRUE),
    READS_OUTLIERS_QUALITY("readsQualityOutliers", Type.FastQ, TRUE),
    READS_2D_OUTLIERS_QUALITY("reads2DQualityOutliers", Type.Fast5, TRUE),
    READS_TEMPLATE_OUTLIERS_QUALITY("readsTemplateQualityOutliers", Type.Fast5, TRUE),
    READS_COMPLEMENT_OUTLIERS_QUALITY("readsComplementQualityOutliers", Type.Fast5, TRUE);


    private String value;
    private Type type;
    private Boolean notSaved = FALSE;

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

    RDataEnum(final String value, final Type type, final Boolean notSaved) {
        this.value = value;
        this.type = type;
        this.notSaved = notSaved;
    }

    public String getValue() {
        return this.value;
    }

    public Type getType() {
        return this.type;
    }

    public Boolean isNotSaved() {
        return this.notSaved;
    }

}
