package com.nanoporeqc.fast5analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsDto {

    private String name;

    private String xName;

    private List<String> yNames;

    private Integer xPrecision;

    private Integer yPrecision;

}
