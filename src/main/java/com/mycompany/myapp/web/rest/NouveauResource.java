package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.repository.NouveauRepository;
import com.mycompany.myapp.service.NouveauService;
import com.mycompany.myapp.service.dto.NouveauDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Nouveau}.
 */
@RestController
@RequestMapping("/api")
public class NouveauResource {

    private final Logger log = LoggerFactory.getLogger(NouveauResource.class);

    private static final String ENTITY_NAME = "nouveau";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NouveauService nouveauService;

    private final NouveauRepository nouveauRepository;

    public NouveauResource(NouveauService nouveauService, NouveauRepository nouveauRepository) {
        this.nouveauService = nouveauService;
        this.nouveauRepository = nouveauRepository;
    }

    /**
     * {@code POST  /nouveaus} : Create a new nouveau.
     *
     * @param nouveauDTO the nouveauDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nouveauDTO, or with status {@code 400 (Bad Request)} if the nouveau has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nouveaus")
    public ResponseEntity<NouveauDTO> createNouveau(@Valid @RequestBody NouveauDTO nouveauDTO) throws URISyntaxException {
        log.debug("REST request to save Nouveau : {}", nouveauDTO);
        if (nouveauDTO.getId() != null) {
            throw new BadRequestAlertException("A new nouveau cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NouveauDTO result = nouveauService.save(nouveauDTO);
        return ResponseEntity
            .created(new URI("/api/nouveaus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nouveaus/:id} : Updates an existing nouveau.
     *
     * @param id the id of the nouveauDTO to save.
     * @param nouveauDTO the nouveauDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nouveauDTO,
     * or with status {@code 400 (Bad Request)} if the nouveauDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nouveauDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nouveaus/{id}")
    public ResponseEntity<NouveauDTO> updateNouveau(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NouveauDTO nouveauDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Nouveau : {}, {}", id, nouveauDTO);
        if (nouveauDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nouveauDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nouveauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NouveauDTO result = nouveauService.save(nouveauDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nouveauDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nouveaus/:id} : Partial updates given fields of an existing nouveau, field will ignore if it is null
     *
     * @param id the id of the nouveauDTO to save.
     * @param nouveauDTO the nouveauDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nouveauDTO,
     * or with status {@code 400 (Bad Request)} if the nouveauDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nouveauDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nouveauDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nouveaus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NouveauDTO> partialUpdateNouveau(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NouveauDTO nouveauDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Nouveau partially : {}, {}", id, nouveauDTO);
        if (nouveauDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nouveauDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nouveauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NouveauDTO> result = nouveauService.partialUpdate(nouveauDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nouveauDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nouveaus} : get all the nouveaus.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nouveaus in body.
     */
    @GetMapping("/nouveaus")
    public ResponseEntity<List<NouveauDTO>> getAllNouveaus(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Nouveaus");
        Page<NouveauDTO> page;
        if (eagerload) {
            page = nouveauService.findAllWithEagerRelationships(pageable);
        } else {
            page = nouveauService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nouveaus/:id} : get the "id" nouveau.
     *
     * @param id the id of the nouveauDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nouveauDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nouveaus/{id}")
    public ResponseEntity<NouveauDTO> getNouveau(@PathVariable Long id) {
        log.debug("REST request to get Nouveau : {}", id);
        Optional<NouveauDTO> nouveauDTO = nouveauService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nouveauDTO);
    }

    /**
     * {@code DELETE  /nouveaus/:id} : delete the "id" nouveau.
     *
     * @param id the id of the nouveauDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nouveaus/{id}")
    public ResponseEntity<Void> deleteNouveau(@PathVariable Long id) {
        log.debug("REST request to delete Nouveau : {}", id);
        nouveauService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/nouveaus?query=:query} : search for the nouveau corresponding
     * to the query.
     *
     * @param query the query of the nouveau search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/nouveaus")
    public ResponseEntity<List<NouveauDTO>> searchNouveaus(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Nouveaus for query {}", query);
        Page<NouveauDTO> page = nouveauService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
