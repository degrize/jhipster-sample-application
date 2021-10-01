package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.GuardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Guard}.
 */
public interface GuardService {
    /**
     * Save a guard.
     *
     * @param guardDTO the entity to save.
     * @return the persisted entity.
     */
    GuardDTO save(GuardDTO guardDTO);

    /**
     * Partially updates a guard.
     *
     * @param guardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GuardDTO> partialUpdate(GuardDTO guardDTO);

    /**
     * Get all the guards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GuardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" guard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GuardDTO> findOne(Long id);

    /**
     * Delete the "id" guard.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the guard corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GuardDTO> search(String query, Pageable pageable);
}
