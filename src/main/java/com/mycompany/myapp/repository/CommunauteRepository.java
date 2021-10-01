package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Communaute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Communaute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunauteRepository extends JpaRepository<Communaute, Long> {}
