package com.nanoporeqc.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusteringReadsDto {

    private Integer id;

    private Double mean;

    private Double median;

    private Double quantile25;

    private Double quantile75;

    private Double outliersRatio;

    private String readsIndices;

}
