package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.FrereQuiInviteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.FrereQuiInvite}.
 */
public interface FrereQuiInviteService {
    /**
     * Save a frereQuiInvite.
     *
     * @param frereQuiInviteDTO the entity to save.
     * @return the persisted entity.
     */
    FrereQuiInviteDTO save(FrereQuiInviteDTO frereQuiInviteDTO);

    /**
     * Partially updates a frereQuiInvite.
     *
     * @param frereQuiInviteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FrereQuiInviteDTO> partialUpdate(FrereQuiInviteDTO frereQuiInviteDTO);

    /**
     * Get all the frereQuiInvites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FrereQuiInviteDTO> findAll(Pageable pageable);

    /**
     * Get all the frereQuiInvites with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FrereQuiInviteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" frereQuiInvite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FrereQuiInviteDTO> findOne(Long id);

    /**
     * Delete the "id" frereQuiInvite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the frereQuiInvite corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FrereQuiInviteDTO> search(String query, Pageable pageable);
}
