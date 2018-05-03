package com.nanoporeqc.fast5analyse.domain;

import com.nanoporeqc.user.domain.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.sql.Blob;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ANALYSES")
public class Analyse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private ApplicationUser user;
}
