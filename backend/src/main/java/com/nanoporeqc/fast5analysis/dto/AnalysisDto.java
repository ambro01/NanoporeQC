package com.nanoporeqc.fast5analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisDto {

    private Long id;

    @Max(50)
    private String name;

    @Max(150)
    private String comment;

    private String runTime;

    private String type;

}
