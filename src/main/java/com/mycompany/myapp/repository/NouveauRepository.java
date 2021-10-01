package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Nouveau;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Nouveau entity.
 */
@Repository
public interface NouveauRepository extends JpaRepository<Nouveau, Long> {
    @Query(
        value = "select distinct nouveau from Nouveau nouveau left join fetch nouveau.departements left join fetch nouveau.frereQuiInvites left join fetch nouveau.besoins left join fetch nouveau.decisions",
        countQuery = "select count(distinct nouveau) from Nouveau nouveau"
    )
    Page<Nouveau> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct nouveau from Nouveau nouveau left join fetch nouveau.departements left join fetch nouveau.frereQuiInvites left join fetch nouveau.besoins left join fetch nouveau.decisions"
    )
    List<Nouveau> findAllWithEagerRelationships();

    @Query(
        "select nouveau from Nouveau nouveau left join fetch nouveau.departements left join fetch nouveau.frereQuiInvites left join fetch nouveau.besoins left join fetch nouveau.decisions where nouveau.id =:id"
    )
    Optional<Nouveau> findOneWithEagerRelationships(@Param("id") Long id);
}
