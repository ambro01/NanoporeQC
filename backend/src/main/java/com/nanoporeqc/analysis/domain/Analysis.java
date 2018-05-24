package com.nanoporeqc.analysis.domain;

import com.nanoporeqc.user.domain.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "RUN_TIME")
    private LocalDateTime runTime;

    @Size(max = 50)
    @Column(name = "NAME")
    private String name;

    @Size(max = 150)
    @Column(name = "COMMENT")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private Type type;

    @Column(name = "SUMMARY_FILE_CONTENT")
    private Blob content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private ApplicationUser user;

}
