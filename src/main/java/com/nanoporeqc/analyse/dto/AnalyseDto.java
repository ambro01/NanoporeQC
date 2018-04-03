package com.nanoporeqc.analyse.dto;

import lombok.Data;

import javax.validation.constraints.Max;

@Data
public class AnalyseDto {
    @Max(50)
    String analyseName;
}
