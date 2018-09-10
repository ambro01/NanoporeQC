package com.nanoporeqc.csv.utils;

import com.nanoporeqc.r.consts.RDataConst;
import com.nanoporeqc.r.enumeration.RDataEnum;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class CsvUtils {

    private CsvUtils() {
    }

    public static List<String> getSetOfVariables(final RDataEnum rDataEnum) {
        return RDataConst.RDataMap
                .get(rDataEnum)
                .getRVariablesMap()
                .entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .collect(toList());
    }
}
