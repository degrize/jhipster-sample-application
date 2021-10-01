package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.NouveauDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Nouveau}.
 */
public interface NouveauService {
    /**
     * Save a nouveau.
     *
     * @param nouveauDTO the entity to save.
     * @return the persisted entity.
     */
    NouveauDTO save(NouveauDTO nouveauDTO);

    /**
     * Partially updates a nouveau.
     *
     * @param nouveauDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NouveauDTO> partialUpdate(NouveauDTO nouveauDTO);

    /**
     * Get all the nouveaus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NouveauDTO> findAll(Pageable pageable);

    /**
     * Get all the nouveaus with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NouveauDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" nouveau.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NouveauDTO> findOne(Long id);

    /**
     * Delete the "id" nouveau.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the nouveau corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NouveauDTO> search(String query, Pageable pageable);
}
