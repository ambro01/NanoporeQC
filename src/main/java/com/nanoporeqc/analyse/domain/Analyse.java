package com.nanoporeqc.analyse.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.sql.Blob;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ANALYSE_ENTITY")
public class Analyse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ANALYSE_ID")
    private Long id;

    @Column(name = "ANALYSE_TIME")
    private LocalDateTime runTime;

    @Size(max = 50)
    @Column(name = "ANALYSE_NAME")
    private String name;

    @Size(max = 150)
    @Column(name = "ANALYSE_COMMENT")
    private String comment;

    @Column(name = "SUMMARY_FILE_CONTENT")
    private Blob content;
}
