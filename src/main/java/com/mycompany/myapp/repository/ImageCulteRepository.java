package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ImageCulte;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ImageCulte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageCulteRepository extends JpaRepository<ImageCulte, Long> {}
