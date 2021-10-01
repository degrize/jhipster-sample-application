package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.repository.FrereQuiInviteRepository;
import com.mycompany.myapp.service.FrereQuiInviteService;
import com.mycompany.myapp.service.dto.FrereQuiInviteDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.FrereQuiInvite}.
 */
@RestController
@RequestMapping("/api")
public class FrereQuiInviteResource {

    private final Logger log = LoggerFactory.getLogger(FrereQuiInviteResource.class);

    private static final String ENTITY_NAME = "frereQuiInvite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrereQuiInviteService frereQuiInviteService;

    private final FrereQuiInviteRepository frereQuiInviteRepository;

    public FrereQuiInviteResource(FrereQuiInviteService frereQuiInviteService, FrereQuiInviteRepository frereQuiInviteRepository) {
        this.frereQuiInviteService = frereQuiInviteService;
        this.frereQuiInviteRepository = frereQuiInviteRepository;
    }

    /**
     * {@code POST  /frere-qui-invites} : Create a new frereQuiInvite.
     *
     * @param frereQuiInviteDTO the frereQuiInviteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new frereQuiInviteDTO, or with status {@code 400 (Bad Request)} if the frereQuiInvite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/frere-qui-invites")
    public ResponseEntity<FrereQuiInviteDTO> createFrereQuiInvite(@Valid @RequestBody FrereQuiInviteDTO frereQuiInviteDTO)
        throws URISyntaxException {
        log.debug("REST request to save FrereQuiInvite : {}", frereQuiInviteDTO);
        if (frereQuiInviteDTO.getId() != null) {
            throw new BadRequestAlertException("A new frereQuiInvite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FrereQuiInviteDTO result = frereQuiInviteService.save(frereQuiInviteDTO);
        return ResponseEntity
            .created(new URI("/api/frere-qui-invites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frere-qui-invites/:id} : Updates an existing frereQuiInvite.
     *
     * @param id the id of the frereQuiInviteDTO to save.
     * @param frereQuiInviteDTO the frereQuiInviteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frereQuiInviteDTO,
     * or with status {@code 400 (Bad Request)} if the frereQuiInviteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the frereQuiInviteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/frere-qui-invites/{id}")
    public ResponseEntity<FrereQuiInviteDTO> updateFrereQuiInvite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FrereQuiInviteDTO frereQuiInviteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FrereQuiInvite : {}, {}", id, frereQuiInviteDTO);
        if (frereQuiInviteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frereQuiInviteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frereQuiInviteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FrereQuiInviteDTO result = frereQuiInviteService.save(frereQuiInviteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frereQuiInviteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /frere-qui-invites/:id} : Partial updates given fields of an existing frereQuiInvite, field will ignore if it is null
     *
     * @param id the id of the frereQuiInviteDTO to save.
     * @param frereQuiInviteDTO the frereQuiInviteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frereQuiInviteDTO,
     * or with status {@code 400 (Bad Request)} if the frereQuiInviteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the frereQuiInviteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the frereQuiInviteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/frere-qui-invites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FrereQuiInviteDTO> partialUpdateFrereQuiInvite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FrereQuiInviteDTO frereQuiInviteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FrereQuiInvite partially : {}, {}", id, frereQuiInviteDTO);
        if (frereQuiInviteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frereQuiInviteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frereQuiInviteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FrereQuiInviteDTO> result = frereQuiInviteService.partialUpdate(frereQuiInviteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frereQuiInviteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /frere-qui-invites} : get all the frereQuiInvites.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frereQuiInvites in body.
     */
    @GetMapping("/frere-qui-invites")
    public ResponseEntity<List<FrereQuiInviteDTO>> getAllFrereQuiInvites(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of FrereQuiInvites");
        Page<FrereQuiInviteDTO> page;
        if (eagerload) {
            page = frereQuiInviteService.findAllWithEagerRelationships(pageable);
        } else {
            page = frereQuiInviteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /frere-qui-invites/:id} : get the "id" frereQuiInvite.
     *
     * @param id the id of the frereQuiInviteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frereQuiInviteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/frere-qui-invites/{id}")
    public ResponseEntity<FrereQuiInviteDTO> getFrereQuiInvite(@PathVariable Long id) {
        log.debug("REST request to get FrereQuiInvite : {}", id);
        Optional<FrereQuiInviteDTO> frereQuiInviteDTO = frereQuiInviteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(frereQuiInviteDTO);
    }

    /**
     * {@code DELETE  /frere-qui-invites/:id} : delete the "id" frereQuiInvite.
     *
     * @param id the id of the frereQuiInviteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/frere-qui-invites/{id}")
    public ResponseEntity<Void> deleteFrereQuiInvite(@PathVariable Long id) {
        log.debug("REST request to delete FrereQuiInvite : {}", id);
        frereQuiInviteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/frere-qui-invites?query=:query} : search for the frereQuiInvite corresponding
     * to the query.
     *
     * @param query the query of the frereQuiInvite search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/frere-qui-invites")
    public ResponseEntity<List<FrereQuiInviteDTO>> searchFrereQuiInvites(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FrereQuiInvites for query {}", query);
        Page<FrereQuiInviteDTO> page = frereQuiInviteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
