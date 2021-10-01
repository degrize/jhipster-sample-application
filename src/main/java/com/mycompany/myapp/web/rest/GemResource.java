package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.repository.GemRepository;
import com.mycompany.myapp.service.GemService;
import com.mycompany.myapp.service.dto.GemDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Gem}.
 */
@RestController
@RequestMapping("/api")
public class GemResource {

    private final Logger log = LoggerFactory.getLogger(GemResource.class);

    private static final String ENTITY_NAME = "gem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GemService gemService;

    private final GemRepository gemRepository;

    public GemResource(GemService gemService, GemRepository gemRepository) {
        this.gemService = gemService;
        this.gemRepository = gemRepository;
    }

    /**
     * {@code POST  /gems} : Create a new gem.
     *
     * @param gemDTO the gemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gemDTO, or with status {@code 400 (Bad Request)} if the gem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gems")
    public ResponseEntity<GemDTO> createGem(@Valid @RequestBody GemDTO gemDTO) throws URISyntaxException {
        log.debug("REST request to save Gem : {}", gemDTO);
        if (gemDTO.getId() != null) {
            throw new BadRequestAlertException("A new gem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GemDTO result = gemService.save(gemDTO);
        return ResponseEntity
            .created(new URI("/api/gems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gems/:id} : Updates an existing gem.
     *
     * @param id the id of the gemDTO to save.
     * @param gemDTO the gemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gemDTO,
     * or with status {@code 400 (Bad Request)} if the gemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gems/{id}")
    public ResponseEntity<GemDTO> updateGem(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody GemDTO gemDTO)
        throws URISyntaxException {
        log.debug("REST request to update Gem : {}, {}", id, gemDTO);
        if (gemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GemDTO result = gemService.save(gemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gems/:id} : Partial updates given fields of an existing gem, field will ignore if it is null
     *
     * @param id the id of the gemDTO to save.
     * @param gemDTO the gemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gemDTO,
     * or with status {@code 400 (Bad Request)} if the gemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GemDTO> partialUpdateGem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GemDTO gemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gem partially : {}, {}", id, gemDTO);
        if (gemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GemDTO> result = gemService.partialUpdate(gemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gems} : get all the gems.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gems in body.
     */
    @GetMapping("/gems")
    public ResponseEntity<List<GemDTO>> getAllGems(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Gems");
        Page<GemDTO> page;
        if (eagerload) {
            page = gemService.findAllWithEagerRelationships(pageable);
        } else {
            page = gemService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gems/:id} : get the "id" gem.
     *
     * @param id the id of the gemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gems/{id}")
    public ResponseEntity<GemDTO> getGem(@PathVariable Long id) {
        log.debug("REST request to get Gem : {}", id);
        Optional<GemDTO> gemDTO = gemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gemDTO);
    }

    /**
     * {@code DELETE  /gems/:id} : delete the "id" gem.
     *
     * @param id the id of the gemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gems/{id}")
    public ResponseEntity<Void> deleteGem(@PathVariable Long id) {
        log.debug("REST request to delete Gem : {}", id);
        gemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/gems?query=:query} : search for the gem corresponding
     * to the query.
     *
     * @param query the query of the gem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/gems")
    public ResponseEntity<List<GemDTO>> searchGems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Gems for query {}", query);
        Page<GemDTO> page = gemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
