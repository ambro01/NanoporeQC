package com.nanoporeqc.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisDto {

    private Long id;

    @Max(50)
    @NotNull
    @NotEmpty
    private String name;

    @Max(150)
    private String comment;

    private String runTime;

    @NotNull
    @NotEmpty
    private String type;

    private Boolean hasHtmlReport;

}
