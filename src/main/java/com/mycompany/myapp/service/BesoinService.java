package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.BesoinDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Besoin}.
 */
public interface BesoinService {
    /**
     * Save a besoin.
     *
     * @param besoinDTO the entity to save.
     * @return the persisted entity.
     */
    BesoinDTO save(BesoinDTO besoinDTO);

    /**
     * Partially updates a besoin.
     *
     * @param besoinDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BesoinDTO> partialUpdate(BesoinDTO besoinDTO);

    /**
     * Get all the besoins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BesoinDTO> findAll(Pageable pageable);

    /**
     * Get the "id" besoin.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BesoinDTO> findOne(Long id);

    /**
     * Delete the "id" besoin.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the besoin corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BesoinDTO> search(String query, Pageable pageable);
}
