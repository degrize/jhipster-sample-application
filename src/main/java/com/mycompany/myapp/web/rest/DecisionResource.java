package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.repository.DecisionRepository;
import com.mycompany.myapp.service.DecisionService;
import com.mycompany.myapp.service.dto.DecisionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Decision}.
 */
@RestController
@RequestMapping("/api")
public class DecisionResource {

    private final Logger log = LoggerFactory.getLogger(DecisionResource.class);

    private static final String ENTITY_NAME = "decision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DecisionService decisionService;

    private final DecisionRepository decisionRepository;

    public DecisionResource(DecisionService decisionService, DecisionRepository decisionRepository) {
        this.decisionService = decisionService;
        this.decisionRepository = decisionRepository;
    }

    /**
     * {@code POST  /decisions} : Create a new decision.
     *
     * @param decisionDTO the decisionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new decisionDTO, or with status {@code 400 (Bad Request)} if the decision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/decisions")
    public ResponseEntity<DecisionDTO> createDecision(@Valid @RequestBody DecisionDTO decisionDTO) throws URISyntaxException {
        log.debug("REST request to save Decision : {}", decisionDTO);
        if (decisionDTO.getId() != null) {
            throw new BadRequestAlertException("A new decision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DecisionDTO result = decisionService.save(decisionDTO);
        return ResponseEntity
            .created(new URI("/api/decisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /decisions/:id} : Updates an existing decision.
     *
     * @param id the id of the decisionDTO to save.
     * @param decisionDTO the decisionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decisionDTO,
     * or with status {@code 400 (Bad Request)} if the decisionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the decisionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/decisions/{id}")
    public ResponseEntity<DecisionDTO> updateDecision(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DecisionDTO decisionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Decision : {}, {}", id, decisionDTO);
        if (decisionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decisionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DecisionDTO result = decisionService.save(decisionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decisionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /decisions/:id} : Partial updates given fields of an existing decision, field will ignore if it is null
     *
     * @param id the id of the decisionDTO to save.
     * @param decisionDTO the decisionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decisionDTO,
     * or with status {@code 400 (Bad Request)} if the decisionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the decisionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the decisionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/decisions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DecisionDTO> partialUpdateDecision(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DecisionDTO decisionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Decision partially : {}, {}", id, decisionDTO);
        if (decisionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decisionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DecisionDTO> result = decisionService.partialUpdate(decisionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decisionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /decisions} : get all the decisions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decisions in body.
     */
    @GetMapping("/decisions")
    public ResponseEntity<List<DecisionDTO>> getAllDecisions(Pageable pageable) {
        log.debug("REST request to get a page of Decisions");
        Page<DecisionDTO> page = decisionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /decisions/:id} : get the "id" decision.
     *
     * @param id the id of the decisionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the decisionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/decisions/{id}")
    public ResponseEntity<DecisionDTO> getDecision(@PathVariable Long id) {
        log.debug("REST request to get Decision : {}", id);
        Optional<DecisionDTO> decisionDTO = decisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(decisionDTO);
    }

    /**
     * {@code DELETE  /decisions/:id} : delete the "id" decision.
     *
     * @param id the id of the decisionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/decisions/{id}")
    public ResponseEntity<Void> deleteDecision(@PathVariable Long id) {
        log.debug("REST request to delete Decision : {}", id);
        decisionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/decisions?query=:query} : search for the decision corresponding
     * to the query.
     *
     * @param query the query of the decision search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/decisions")
    public ResponseEntity<List<DecisionDTO>> searchDecisions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Decisions for query {}", query);
        Page<DecisionDTO> page = decisionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
