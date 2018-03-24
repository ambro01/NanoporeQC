package com.nanoporeqc.r.domain;

import com.nanoporeqc.r.enumeration.RVariableTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class RVariable {
    String name;
    RVariableTypeEnum type;
    List rDataSet;

    public RVariable(String name, RVariableTypeEnum type) {
        this.name = name;
        this.type = type;
    }
}
