package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CommunauteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Communaute}.
 */
public interface CommunauteService {
    /**
     * Save a communaute.
     *
     * @param communauteDTO the entity to save.
     * @return the persisted entity.
     */
    CommunauteDTO save(CommunauteDTO communauteDTO);

    /**
     * Partially updates a communaute.
     *
     * @param communauteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommunauteDTO> partialUpdate(CommunauteDTO communauteDTO);

    /**
     * Get all the communautes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommunauteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" communaute.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommunauteDTO> findOne(Long id);

    /**
     * Delete the "id" communaute.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the communaute corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommunauteDTO> search(String query, Pageable pageable);
}
