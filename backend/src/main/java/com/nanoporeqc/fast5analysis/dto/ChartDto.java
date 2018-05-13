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
public class ChartDto {

    private List xValues;

    private List<List> yValuesList;

    private List<List> zValuesList;

}