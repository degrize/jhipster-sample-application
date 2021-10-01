package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FrereQuiInvite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FrereQuiInvite entity.
 */
@Repository
public interface FrereQuiInviteRepository extends JpaRepository<FrereQuiInvite, Long> {
    @Query(
        value = "select distinct frereQuiInvite from FrereQuiInvite frereQuiInvite left join fetch frereQuiInvite.departements",
        countQuery = "select count(distinct frereQuiInvite) from FrereQuiInvite frereQuiInvite"
    )
    Page<FrereQuiInvite> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct frereQuiInvite from FrereQuiInvite frereQuiInvite left join fetch frereQuiInvite.departements")
    List<FrereQuiInvite> findAllWithEagerRelationships();

    @Query(
        "select frereQuiInvite from FrereQuiInvite frereQuiInvite left join fetch frereQuiInvite.departements where frereQuiInvite.id =:id"
    )
    Optional<FrereQuiInvite> findOneWithEagerRelationships(@Param("id") Long id);
}
