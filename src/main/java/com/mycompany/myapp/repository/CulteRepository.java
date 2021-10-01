package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Culte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Culte entity.
 */
@Repository
public interface CulteRepository extends JpaRepository<Culte, Long> {
    @Query(
        value = "select distinct culte from Culte culte left join fetch culte.imageCultes",
        countQuery = "select count(distinct culte) from Culte culte"
    )
    Page<Culte> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct culte from Culte culte left join fetch culte.imageCultes")
    List<Culte> findAllWithEagerRelationships();

    @Query("select culte from Culte culte left join fetch culte.imageCultes where culte.id =:id")
    Optional<Culte> findOneWithEagerRelationships(@Param("id") Long id);
}
