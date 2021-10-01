package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Besoin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Besoin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BesoinRepository extends JpaRepository<Besoin, Long> {}
