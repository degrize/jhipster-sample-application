package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.repository.GuardRepository;
import com.mycompany.myapp.service.GuardService;
import com.mycompany.myapp.service.dto.GuardDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Guard}.
 */
@RestController
@RequestMapping("/api")
public class GuardResource {

    private final Logger log = LoggerFactory.getLogger(GuardResource.class);

    private static final String ENTITY_NAME = "guard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GuardService guardService;

    private final GuardRepository guardRepository;

    public GuardResource(GuardService guardService, GuardRepository guardRepository) {
        this.guardService = guardService;
        this.guardRepository = guardRepository;
    }

    /**
     * {@code POST  /guards} : Create a new guard.
     *
     * @param guardDTO the guardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new guardDTO, or with status {@code 400 (Bad Request)} if the guard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/guards")
    public ResponseEntity<GuardDTO> createGuard(@RequestBody GuardDTO guardDTO) throws URISyntaxException {
        log.debug("REST request to save Guard : {}", guardDTO);
        if (guardDTO.getId() != null) {
            throw new BadRequestAlertException("A new guard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GuardDTO result = guardService.save(guardDTO);
        return ResponseEntity
            .created(new URI("/api/guards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /guards/:id} : Updates an existing guard.
     *
     * @param id the id of the guardDTO to save.
     * @param guardDTO the guardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guardDTO,
     * or with status {@code 400 (Bad Request)} if the guardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the guardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/guards/{id}")
    public ResponseEntity<GuardDTO> updateGuard(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GuardDTO guardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Guard : {}, {}", id, guardDTO);
        if (guardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GuardDTO result = guardService.save(guardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /guards/:id} : Partial updates given fields of an existing guard, field will ignore if it is null
     *
     * @param id the id of the guardDTO to save.
     * @param guardDTO the guardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated guardDTO,
     * or with status {@code 400 (Bad Request)} if the guardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the guardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the guardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/guards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GuardDTO> partialUpdateGuard(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GuardDTO guardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Guard partially : {}, {}", id, guardDTO);
        if (guardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, guardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!guardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GuardDTO> result = guardService.partialUpdate(guardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, guardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /guards} : get all the guards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of guards in body.
     */
    @GetMapping("/guards")
    public ResponseEntity<List<GuardDTO>> getAllGuards(Pageable pageable) {
        log.debug("REST request to get a page of Guards");
        Page<GuardDTO> page = guardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /guards/:id} : get the "id" guard.
     *
     * @param id the id of the guardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the guardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/guards/{id}")
    public ResponseEntity<GuardDTO> getGuard(@PathVariable Long id) {
        log.debug("REST request to get Guard : {}", id);
        Optional<GuardDTO> guardDTO = guardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(guardDTO);
    }

    /**
     * {@code DELETE  /guards/:id} : delete the "id" guard.
     *
     * @param id the id of the guardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/guards/{id}")
    public ResponseEntity<Void> deleteGuard(@PathVariable Long id) {
        log.debug("REST request to delete Guard : {}", id);
        guardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/guards?query=:query} : search for the guard corresponding
     * to the query.
     *
     * @param query the query of the guard search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/guards")
    public ResponseEntity<List<GuardDTO>> searchGuards(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Guards for query {}", query);
        Page<GuardDTO> page = guardService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
