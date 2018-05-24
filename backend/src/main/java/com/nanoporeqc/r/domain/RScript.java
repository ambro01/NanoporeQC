package com.nanoporeqc.r.domain;

import com.nanoporeqc.r.enumeration.RScriptEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RScript {

    private RScriptEnum name;

    private Map<String, RVariable> rVariablesMap;
    
}
