package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Quartier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Quartier entity.
 */
@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {
    @Query(
        value = "select distinct quartier from Quartier quartier left join fetch quartier.villes",
        countQuery = "select count(distinct quartier) from Quartier quartier"
    )
    Page<Quartier> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct quartier from Quartier quartier left join fetch quartier.villes")
    List<Quartier> findAllWithEagerRelationships();

    @Query("select quartier from Quartier quartier left join fetch quartier.villes where quartier.id =:id")
    Optional<Quartier> findOneWithEagerRelationships(@Param("id") Long id);
}
