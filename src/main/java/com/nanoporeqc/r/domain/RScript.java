package com.nanoporeqc.r.domain;

import com.nanoporeqc.r.enumeration.RScriptEnum;
import lombok.Data;

import java.util.Map;

@Data
public class RScript {
    RScriptEnum name;
    Map<String, RVariable> rVariablesMap;

    public RScript(RScriptEnum name, Map<String, RVariable> rVariablesMap) {
        this.name = name;
        this.rVariablesMap = rVariablesMap;
    }
}
