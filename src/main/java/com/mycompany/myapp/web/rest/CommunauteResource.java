package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.repository.CommunauteRepository;
import com.mycompany.myapp.service.CommunauteService;
import com.mycompany.myapp.service.dto.CommunauteDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Communaute}.
 */
@RestController
@RequestMapping("/api")
public class CommunauteResource {

    private final Logger log = LoggerFactory.getLogger(CommunauteResource.class);

    private static final String ENTITY_NAME = "communaute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunauteService communauteService;

    private final CommunauteRepository communauteRepository;

    public CommunauteResource(CommunauteService communauteService, CommunauteRepository communauteRepository) {
        this.communauteService = communauteService;
        this.communauteRepository = communauteRepository;
    }

    /**
     * {@code POST  /communautes} : Create a new communaute.
     *
     * @param communauteDTO the communauteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communauteDTO, or with status {@code 400 (Bad Request)} if the communaute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/communautes")
    public ResponseEntity<CommunauteDTO> createCommunaute(@Valid @RequestBody CommunauteDTO communauteDTO) throws URISyntaxException {
        log.debug("REST request to save Communaute : {}", communauteDTO);
        if (communauteDTO.getId() != null) {
            throw new BadRequestAlertException("A new communaute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommunauteDTO result = communauteService.save(communauteDTO);
        return ResponseEntity
            .created(new URI("/api/communautes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /communautes/:id} : Updates an existing communaute.
     *
     * @param id the id of the communauteDTO to save.
     * @param communauteDTO the communauteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communauteDTO,
     * or with status {@code 400 (Bad Request)} if the communauteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communauteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/communautes/{id}")
    public ResponseEntity<CommunauteDTO> updateCommunaute(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommunauteDTO communauteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Communaute : {}, {}", id, communauteDTO);
        if (communauteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communauteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communauteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommunauteDTO result = communauteService.save(communauteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communauteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /communautes/:id} : Partial updates given fields of an existing communaute, field will ignore if it is null
     *
     * @param id the id of the communauteDTO to save.
     * @param communauteDTO the communauteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communauteDTO,
     * or with status {@code 400 (Bad Request)} if the communauteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the communauteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the communauteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/communautes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommunauteDTO> partialUpdateCommunaute(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommunauteDTO communauteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Communaute partially : {}, {}", id, communauteDTO);
        if (communauteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communauteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communauteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunauteDTO> result = communauteService.partialUpdate(communauteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communauteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /communautes} : get all the communautes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communautes in body.
     */
    @GetMapping("/communautes")
    public ResponseEntity<List<CommunauteDTO>> getAllCommunautes(Pageable pageable) {
        log.debug("REST request to get a page of Communautes");
        Page<CommunauteDTO> page = communauteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /communautes/:id} : get the "id" communaute.
     *
     * @param id the id of the communauteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communauteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/communautes/{id}")
    public ResponseEntity<CommunauteDTO> getCommunaute(@PathVariable Long id) {
        log.debug("REST request to get Communaute : {}", id);
        Optional<CommunauteDTO> communauteDTO = communauteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(communauteDTO);
    }

    /**
     * {@code DELETE  /communautes/:id} : delete the "id" communaute.
     *
     * @param id the id of the communauteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/communautes/{id}")
    public ResponseEntity<Void> deleteCommunaute(@PathVariable Long id) {
        log.debug("REST request to delete Communaute : {}", id);
        communauteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/communautes?query=:query} : search for the communaute corresponding
     * to the query.
     *
     * @param query the query of the communaute search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/communautes")
    public ResponseEntity<List<CommunauteDTO>> searchCommunautes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Communautes for query {}", query);
        Page<CommunauteDTO> page = communauteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
