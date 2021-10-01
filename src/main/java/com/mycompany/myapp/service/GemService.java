package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.GemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Gem}.
 */
public interface GemService {
    /**
     * Save a gem.
     *
     * @param gemDTO the entity to save.
     * @return the persisted entity.
     */
    GemDTO save(GemDTO gemDTO);

    /**
     * Partially updates a gem.
     *
     * @param gemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GemDTO> partialUpdate(GemDTO gemDTO);

    /**
     * Get all the gems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GemDTO> findAll(Pageable pageable);

    /**
     * Get all the gems with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GemDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" gem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GemDTO> findOne(Long id);

    /**
     * Delete the "id" gem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the gem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GemDTO> search(String query, Pageable pageable);
}
