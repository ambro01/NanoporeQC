package com.nanoporeqc;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChartDto {
    List xValues;
    List<List> yValuesList;
    List<List> zValuesList;
}
