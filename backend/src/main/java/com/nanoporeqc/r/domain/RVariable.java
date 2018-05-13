package com.nanoporeqc.r.domain;

import com.nanoporeqc.r.enumeration.RVariableTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RVariable {
    private String name;
    private RVariableTypeEnum type;
    private List rDataSet;
    private Integer precision;

    public RVariable(String name, RVariableTypeEnum type) {
        this.name = name;
        this.type = type;
    }
    public RVariable(String name, RVariableTypeEnum type, Integer precision) {
        this.name = name;
        this.type = type;
        this.precision = precision;
    }

}
