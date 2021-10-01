package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ImageCulteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ImageCulte}.
 */
public interface ImageCulteService {
    /**
     * Save a imageCulte.
     *
     * @param imageCulteDTO the entity to save.
     * @return the persisted entity.
     */
    ImageCulteDTO save(ImageCulteDTO imageCulteDTO);

    /**
     * Partially updates a imageCulte.
     *
     * @param imageCulteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImageCulteDTO> partialUpdate(ImageCulteDTO imageCulteDTO);

    /**
     * Get all the imageCultes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImageCulteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" imageCulte.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImageCulteDTO> findOne(Long id);

    /**
     * Delete the "id" imageCulte.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the imageCulte corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImageCulteDTO> search(String query, Pageable pageable);
}
