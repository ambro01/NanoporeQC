package com.nanoporeqc.r.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummaryInfoDto {
    private Integer id;
    private String fileName;
    private Integer strandIndexInChannel;
    private Integer channelIndex;
    private Double startTime;
    private Double duration;
    private Integer eventsNo;
    private String hasTemplate;
    private Integer eventsNoTemplate;
    private String hasComplement;
    private Integer eventsNoComplement;
    private String is2d;
}
