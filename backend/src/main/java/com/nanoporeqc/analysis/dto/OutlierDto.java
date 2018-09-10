package com.nanoporeqc.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutlierDto {

    private Integer outlierPlace;

    private String name;

    private Integer readId;

    private Double distanceFromCentre;

}
