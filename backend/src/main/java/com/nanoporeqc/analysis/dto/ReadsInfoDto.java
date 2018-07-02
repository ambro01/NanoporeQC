package com.nanoporeqc.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadsInfoDto {

    private Integer id;

    private String name;

    private Double mean;

    private Double median;

    private Double quantile25;

    private Double quantile75;

    private Double outliersRatio;

    private Integer count;

}
