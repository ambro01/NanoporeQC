package com.nanoporeqc.fast5analyse.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import java.text.SimpleDateFormat;

@Data
public class AnalyseDto {

    private Long id;

    @Max(50)
    private String name;

    @Max(150)
    private String comment;

    private String runTime;

}
