package com.nanoporeqc.analysis.repository;

import com.nanoporeqc.analysis.domain.Analysis;
import com.nanoporeqc.analysis.domain.QualityStatus;
import com.nanoporeqc.analysis.domain.Type;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface AnalysisRepository extends CrudRepository<Analysis, Long> {

    List<Analysis> findAllByUserId(@NotNull final Long userId);

    Long countByType(@NotNull final Type type);

    Long countByTypeAndUserRate(@NotNull final Type type, @NotNull final QualityStatus userRate);

    Analysis findFirstByTypeOrderByRunTimeDesc(@NotNull final Type type);

    Analysis findFirstByTypeAndUserRateOrderByRunTimeDesc(@NotNull final Type type, @NotNull final QualityStatus userRate);

}
