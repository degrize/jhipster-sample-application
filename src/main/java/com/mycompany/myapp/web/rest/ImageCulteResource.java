package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.repository.ImageCulteRepository;
import com.mycompany.myapp.service.ImageCulteService;
import com.mycompany.myapp.service.dto.ImageCulteDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ImageCulte}.
 */
@RestController
@RequestMapping("/api")
public class ImageCulteResource {

    private final Logger log = LoggerFactory.getLogger(ImageCulteResource.class);

    private static final String ENTITY_NAME = "imageCulte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageCulteService imageCulteService;

    private final ImageCulteRepository imageCulteRepository;

    public ImageCulteResource(ImageCulteService imageCulteService, ImageCulteRepository imageCulteRepository) {
        this.imageCulteService = imageCulteService;
        this.imageCulteRepository = imageCulteRepository;
    }

    /**
     * {@code POST  /image-cultes} : Create a new imageCulte.
     *
     * @param imageCulteDTO the imageCulteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageCulteDTO, or with status {@code 400 (Bad Request)} if the imageCulte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/image-cultes")
    public ResponseEntity<ImageCulteDTO> createImageCulte(@Valid @RequestBody ImageCulteDTO imageCulteDTO) throws URISyntaxException {
        log.debug("REST request to save ImageCulte : {}", imageCulteDTO);
        if (imageCulteDTO.getId() != null) {
            throw new BadRequestAlertException("A new imageCulte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImageCulteDTO result = imageCulteService.save(imageCulteDTO);
        return ResponseEntity
            .created(new URI("/api/image-cultes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /image-cultes/:id} : Updates an existing imageCulte.
     *
     * @param id the id of the imageCulteDTO to save.
     * @param imageCulteDTO the imageCulteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageCulteDTO,
     * or with status {@code 400 (Bad Request)} if the imageCulteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageCulteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/image-cultes/{id}")
    public ResponseEntity<ImageCulteDTO> updateImageCulte(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImageCulteDTO imageCulteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ImageCulte : {}, {}", id, imageCulteDTO);
        if (imageCulteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageCulteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageCulteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImageCulteDTO result = imageCulteService.save(imageCulteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageCulteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /image-cultes/:id} : Partial updates given fields of an existing imageCulte, field will ignore if it is null
     *
     * @param id the id of the imageCulteDTO to save.
     * @param imageCulteDTO the imageCulteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageCulteDTO,
     * or with status {@code 400 (Bad Request)} if the imageCulteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imageCulteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageCulteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/image-cultes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageCulteDTO> partialUpdateImageCulte(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImageCulteDTO imageCulteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImageCulte partially : {}, {}", id, imageCulteDTO);
        if (imageCulteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageCulteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageCulteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageCulteDTO> result = imageCulteService.partialUpdate(imageCulteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageCulteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /image-cultes} : get all the imageCultes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageCultes in body.
     */
    @GetMapping("/image-cultes")
    public ResponseEntity<List<ImageCulteDTO>> getAllImageCultes(Pageable pageable) {
        log.debug("REST request to get a page of ImageCultes");
        Page<ImageCulteDTO> page = imageCulteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /image-cultes/:id} : get the "id" imageCulte.
     *
     * @param id the id of the imageCulteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageCulteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/image-cultes/{id}")
    public ResponseEntity<ImageCulteDTO> getImageCulte(@PathVariable Long id) {
        log.debug("REST request to get ImageCulte : {}", id);
        Optional<ImageCulteDTO> imageCulteDTO = imageCulteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageCulteDTO);
    }

    /**
     * {@code DELETE  /image-cultes/:id} : delete the "id" imageCulte.
     *
     * @param id the id of the imageCulteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/image-cultes/{id}")
    public ResponseEntity<Void> deleteImageCulte(@PathVariable Long id) {
        log.debug("REST request to delete ImageCulte : {}", id);
        imageCulteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/image-cultes?query=:query} : search for the imageCulte corresponding
     * to the query.
     *
     * @param query the query of the imageCulte search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/image-cultes")
    public ResponseEntity<List<ImageCulteDTO>> searchImageCultes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ImageCultes for query {}", query);
        Page<ImageCulteDTO> page = imageCulteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
