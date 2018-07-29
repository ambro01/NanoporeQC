package com.nanoporeqc.analysis.dto;

import com.nanoporeqc.analysis.domain.QualityStatus;
import com.nanoporeqc.analysis.domain.Type;
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
    private Type type;

    private QualityStatus userRate;

    private QualityStatus qualityStatus;

    private Boolean hasHtmlReport;

    private Boolean fastQFromFast5;

}
