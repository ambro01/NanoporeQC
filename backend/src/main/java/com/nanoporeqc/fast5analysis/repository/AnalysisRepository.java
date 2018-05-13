package com.nanoporeqc.fast5analysis.repository;

import com.nanoporeqc.fast5analysis.domain.Analysis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface AnalysisRepository extends CrudRepository<Analysis, Long> {
    List<Analysis> findAllByUserId(@NotNull final Long userId);
}
