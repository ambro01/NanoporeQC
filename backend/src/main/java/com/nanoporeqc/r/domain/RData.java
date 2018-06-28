package com.nanoporeqc.r.domain;

import com.nanoporeqc.r.enumeration.RDataEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RData {

    private RDataEnum name;

    private Map<String, RVariable> rVariablesMap;

}
