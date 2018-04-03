package com.nanoporeqc.analyse.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.time.LocalDateTime;

@Data
@Entity
public class Analyse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ANALYSE_ID")
    Long id;

    @Column(name = "ANALYSE_TIME")
    LocalDateTime runTime;

    @Max(50)
    @Column(name = "FILE_NAME")
    String name;

    // przechowywane w /tmp/nanoporeqc/summaryfiles/
//    @Column(name = "FILE_CONTENT")
//    byte[] content;
}
