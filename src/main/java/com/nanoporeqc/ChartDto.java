package com.nanoporeqc;

import lombok.Data;

import java.util.List;

@Data
public class ChartDto {
    List xValues;
    List yValues;
    List zValues;
}
