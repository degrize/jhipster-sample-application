package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.repository.CulteRepository;
import com.mycompany.myapp.service.CulteService;
import com.mycompany.myapp.service.dto.CulteDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Culte}.
 */
@RestController
@RequestMapping("/api")
public class CulteResource {

    private final Logger log = LoggerFactory.getLogger(CulteResource.class);

    private static final String ENTITY_NAME = "culte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CulteService culteService;

    private final CulteRepository culteRepository;

    public CulteResource(CulteService culteService, CulteRepository culteRepository) {
        this.culteService = culteService;
        this.culteRepository = culteRepository;
    }

    /**
     * {@code POST  /cultes} : Create a new culte.
     *
     * @param culteDTO the culteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new culteDTO, or with status {@code 400 (Bad Request)} if the culte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cultes")
    public ResponseEntity<CulteDTO> createCulte(@Valid @RequestBody CulteDTO culteDTO) throws URISyntaxException {
        log.debug("REST request to save Culte : {}", culteDTO);
        if (culteDTO.getId() != null) {
            throw new BadRequestAlertException("A new culte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CulteDTO result = culteService.save(culteDTO);
        return ResponseEntity
            .created(new URI("/api/cultes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cultes/:id} : Updates an existing culte.
     *
     * @param id the id of the culteDTO to save.
     * @param culteDTO the culteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated culteDTO,
     * or with status {@code 400 (Bad Request)} if the culteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the culteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cultes/{id}")
    public ResponseEntity<CulteDTO> updateCulte(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CulteDTO culteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Culte : {}, {}", id, culteDTO);
        if (culteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, culteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!culteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CulteDTO result = culteService.save(culteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, culteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cultes/:id} : Partial updates given fields of an existing culte, field will ignore if it is null
     *
     * @param id the id of the culteDTO to save.
     * @param culteDTO the culteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated culteDTO,
     * or with status {@code 400 (Bad Request)} if the culteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the culteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the culteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cultes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CulteDTO> partialUpdateCulte(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CulteDTO culteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Culte partially : {}, {}", id, culteDTO);
        if (culteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, culteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!culteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CulteDTO> result = culteService.partialUpdate(culteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, culteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cultes} : get all the cultes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultes in body.
     */
    @GetMapping("/cultes")
    public ResponseEntity<List<CulteDTO>> getAllCultes(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Cultes");
        Page<CulteDTO> page;
        if (eagerload) {
            page = culteService.findAllWithEagerRelationships(pageable);
        } else {
            page = culteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultes/:id} : get the "id" culte.
     *
     * @param id the id of the culteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the culteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cultes/{id}")
    public ResponseEntity<CulteDTO> getCulte(@PathVariable Long id) {
        log.debug("REST request to get Culte : {}", id);
        Optional<CulteDTO> culteDTO = culteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(culteDTO);
    }

    /**
     * {@code DELETE  /cultes/:id} : delete the "id" culte.
     *
     * @param id the id of the culteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cultes/{id}")
    public ResponseEntity<Void> deleteCulte(@PathVariable Long id) {
        log.debug("REST request to delete Culte : {}", id);
        culteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/cultes?query=:query} : search for the culte corresponding
     * to the query.
     *
     * @param query the query of the culte search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/cultes")
    public ResponseEntity<List<CulteDTO>> searchCultes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cultes for query {}", query);
        Page<CulteDTO> page = culteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
