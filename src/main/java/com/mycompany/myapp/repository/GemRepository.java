package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Gem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Gem entity.
 */
@Repository
public interface GemRepository extends JpaRepository<Gem, Long> {
    @Query(
        value = "select distinct gem from Gem gem left join fetch gem.frereQuiInvites",
        countQuery = "select count(distinct gem) from Gem gem"
    )
    Page<Gem> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct gem from Gem gem left join fetch gem.frereQuiInvites")
    List<Gem> findAllWithEagerRelationships();

    @Query("select gem from Gem gem left join fetch gem.frereQuiInvites where gem.id =:id")
    Optional<Gem> findOneWithEagerRelationships(@Param("id") Long id);
}
