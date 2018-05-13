package com.nanoporeqc.fast5analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuplicatedSequenceDto {

    private String sequence;

    private Integer count;

}
