package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.DecisionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Decision}.
 */
public interface DecisionService {
    /**
     * Save a decision.
     *
     * @param decisionDTO the entity to save.
     * @return the persisted entity.
     */
    DecisionDTO save(DecisionDTO decisionDTO);

    /**
     * Partially updates a decision.
     *
     * @param decisionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DecisionDTO> partialUpdate(DecisionDTO decisionDTO);

    /**
     * Get all the decisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DecisionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" decision.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DecisionDTO> findOne(Long id);

    /**
     * Delete the "id" decision.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the decision corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DecisionDTO> search(String query, Pageable pageable);
}
