package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.KeyOperatingPropertyRepository;
import co.com.ies.smol.service.KeyOperatingPropertyQueryService;
import co.com.ies.smol.service.KeyOperatingPropertyService;
import co.com.ies.smol.service.criteria.KeyOperatingPropertyCriteria;
import co.com.ies.smol.service.dto.KeyOperatingPropertyDTO;
import co.com.ies.smol.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.com.ies.smol.domain.KeyOperatingProperty}.
 */
@RestController
@RequestMapping("/api")
public class KeyOperatingPropertyResource {

    private final Logger log = LoggerFactory.getLogger(KeyOperatingPropertyResource.class);

    private static final String ENTITY_NAME = "keyOperatingProperty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KeyOperatingPropertyService keyOperatingPropertyService;

    private final KeyOperatingPropertyRepository keyOperatingPropertyRepository;

    private final KeyOperatingPropertyQueryService keyOperatingPropertyQueryService;

    public KeyOperatingPropertyResource(
        KeyOperatingPropertyService keyOperatingPropertyService,
        KeyOperatingPropertyRepository keyOperatingPropertyRepository,
        KeyOperatingPropertyQueryService keyOperatingPropertyQueryService
    ) {
        this.keyOperatingPropertyService = keyOperatingPropertyService;
        this.keyOperatingPropertyRepository = keyOperatingPropertyRepository;
        this.keyOperatingPropertyQueryService = keyOperatingPropertyQueryService;
    }

    /**
     * {@code POST  /key-operating-properties} : Create a new keyOperatingProperty.
     *
     * @param keyOperatingPropertyDTO the keyOperatingPropertyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new keyOperatingPropertyDTO, or with status {@code 400 (Bad Request)} if the keyOperatingProperty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/key-operating-properties")
    public ResponseEntity<KeyOperatingPropertyDTO> createKeyOperatingProperty(
        @Valid @RequestBody KeyOperatingPropertyDTO keyOperatingPropertyDTO
    ) throws URISyntaxException {
        log.debug("REST request to save KeyOperatingProperty : {}", keyOperatingPropertyDTO);
        if (keyOperatingPropertyDTO.getId() != null) {
            throw new BadRequestAlertException("A new keyOperatingProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KeyOperatingPropertyDTO result = keyOperatingPropertyService.save(keyOperatingPropertyDTO);
        return ResponseEntity
            .created(new URI("/api/key-operating-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /key-operating-properties/:id} : Updates an existing keyOperatingProperty.
     *
     * @param id the id of the keyOperatingPropertyDTO to save.
     * @param keyOperatingPropertyDTO the keyOperatingPropertyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keyOperatingPropertyDTO,
     * or with status {@code 400 (Bad Request)} if the keyOperatingPropertyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the keyOperatingPropertyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/key-operating-properties/{id}")
    public ResponseEntity<KeyOperatingPropertyDTO> updateKeyOperatingProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KeyOperatingPropertyDTO keyOperatingPropertyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update KeyOperatingProperty : {}, {}", id, keyOperatingPropertyDTO);
        if (keyOperatingPropertyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, keyOperatingPropertyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!keyOperatingPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        KeyOperatingPropertyDTO result = keyOperatingPropertyService.update(keyOperatingPropertyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, keyOperatingPropertyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /key-operating-properties/:id} : Partial updates given fields of an existing keyOperatingProperty, field will ignore if it is null
     *
     * @param id the id of the keyOperatingPropertyDTO to save.
     * @param keyOperatingPropertyDTO the keyOperatingPropertyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated keyOperatingPropertyDTO,
     * or with status {@code 400 (Bad Request)} if the keyOperatingPropertyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the keyOperatingPropertyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the keyOperatingPropertyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/key-operating-properties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KeyOperatingPropertyDTO> partialUpdateKeyOperatingProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KeyOperatingPropertyDTO keyOperatingPropertyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update KeyOperatingProperty partially : {}, {}", id, keyOperatingPropertyDTO);
        if (keyOperatingPropertyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, keyOperatingPropertyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!keyOperatingPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KeyOperatingPropertyDTO> result = keyOperatingPropertyService.partialUpdate(keyOperatingPropertyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, keyOperatingPropertyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /key-operating-properties} : get all the keyOperatingProperties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of keyOperatingProperties in body.
     */
    @GetMapping("/key-operating-properties")
    public ResponseEntity<List<KeyOperatingPropertyDTO>> getAllKeyOperatingProperties(
        KeyOperatingPropertyCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get KeyOperatingProperties by criteria: {}", criteria);
        Page<KeyOperatingPropertyDTO> page = keyOperatingPropertyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /key-operating-properties/count} : count all the keyOperatingProperties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/key-operating-properties/count")
    public ResponseEntity<Long> countKeyOperatingProperties(KeyOperatingPropertyCriteria criteria) {
        log.debug("REST request to count KeyOperatingProperties by criteria: {}", criteria);
        return ResponseEntity.ok().body(keyOperatingPropertyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /key-operating-properties/:id} : get the "id" keyOperatingProperty.
     *
     * @param id the id of the keyOperatingPropertyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the keyOperatingPropertyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/key-operating-properties/{id}")
    public ResponseEntity<KeyOperatingPropertyDTO> getKeyOperatingProperty(@PathVariable Long id) {
        log.debug("REST request to get KeyOperatingProperty : {}", id);
        Optional<KeyOperatingPropertyDTO> keyOperatingPropertyDTO = keyOperatingPropertyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(keyOperatingPropertyDTO);
    }

    /**
     * {@code DELETE  /key-operating-properties/:id} : delete the "id" keyOperatingProperty.
     *
     * @param id the id of the keyOperatingPropertyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/key-operating-properties/{id}")
    public ResponseEntity<Void> deleteKeyOperatingProperty(@PathVariable Long id) {
        log.debug("REST request to delete KeyOperatingProperty : {}", id);
        keyOperatingPropertyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
