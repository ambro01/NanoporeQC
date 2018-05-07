package com.nanoporeqc.fast5analyse.repository;

import com.nanoporeqc.fast5analyse.domain.Analyse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface AnalyseRepository extends CrudRepository<Analyse, Long> {
    List<Analyse> findAllByUserId(@NotNull Long userId);
}
