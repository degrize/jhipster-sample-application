package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CulteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Culte}.
 */
public interface CulteService {
    /**
     * Save a culte.
     *
     * @param culteDTO the entity to save.
     * @return the persisted entity.
     */
    CulteDTO save(CulteDTO culteDTO);

    /**
     * Partially updates a culte.
     *
     * @param culteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CulteDTO> partialUpdate(CulteDTO culteDTO);

    /**
     * Get all the cultes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CulteDTO> findAll(Pageable pageable);

    /**
     * Get all the cultes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CulteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" culte.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CulteDTO> findOne(Long id);

    /**
     * Delete the "id" culte.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the culte corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CulteDTO> search(String query, Pageable pageable);
}
