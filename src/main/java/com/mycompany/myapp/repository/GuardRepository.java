package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Guard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Guard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuardRepository extends JpaRepository<Guard, Long> {}
