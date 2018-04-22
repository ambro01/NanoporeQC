package com.nanoporeqc.r.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChartDto {
    private List xValues;
    private List<List> yValuesList;
    private List<List> zValuesList;
}
