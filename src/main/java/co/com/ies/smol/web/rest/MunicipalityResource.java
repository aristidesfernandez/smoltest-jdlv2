package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.MunicipalityRepository;
import co.com.ies.smol.service.MunicipalityQueryService;
import co.com.ies.smol.service.MunicipalityService;
import co.com.ies.smol.service.criteria.MunicipalityCriteria;
import co.com.ies.smol.service.dto.MunicipalityDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.Municipality}.
 */
@RestController
@RequestMapping("/api")
public class MunicipalityResource {

    private final Logger log = LoggerFactory.getLogger(MunicipalityResource.class);

    private static final String ENTITY_NAME = "municipality";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MunicipalityService municipalityService;

    private final MunicipalityRepository municipalityRepository;

    private final MunicipalityQueryService municipalityQueryService;

    public MunicipalityResource(
        MunicipalityService municipalityService,
        MunicipalityRepository municipalityRepository,
        MunicipalityQueryService municipalityQueryService
    ) {
        this.municipalityService = municipalityService;
        this.municipalityRepository = municipalityRepository;
        this.municipalityQueryService = municipalityQueryService;
    }

    /**
     * {@code POST  /municipalities} : Create a new municipality.
     *
     * @param municipalityDTO the municipalityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new municipalityDTO, or with status {@code 400 (Bad Request)} if the municipality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/municipalities")
    public ResponseEntity<MunicipalityDTO> createMunicipality(@Valid @RequestBody MunicipalityDTO municipalityDTO)
        throws URISyntaxException {
        log.debug("REST request to save Municipality : {}", municipalityDTO);
        if (municipalityDTO.getId() != null) {
            throw new BadRequestAlertException("A new municipality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MunicipalityDTO result = municipalityService.save(municipalityDTO);
        return ResponseEntity
            .created(new URI("/api/municipalities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /municipalities/:id} : Updates an existing municipality.
     *
     * @param id the id of the municipalityDTO to save.
     * @param municipalityDTO the municipalityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated municipalityDTO,
     * or with status {@code 400 (Bad Request)} if the municipalityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the municipalityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/municipalities/{id}")
    public ResponseEntity<MunicipalityDTO> updateMunicipality(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MunicipalityDTO municipalityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Municipality : {}, {}", id, municipalityDTO);
        if (municipalityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, municipalityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!municipalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MunicipalityDTO result = municipalityService.update(municipalityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, municipalityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /municipalities/:id} : Partial updates given fields of an existing municipality, field will ignore if it is null
     *
     * @param id the id of the municipalityDTO to save.
     * @param municipalityDTO the municipalityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated municipalityDTO,
     * or with status {@code 400 (Bad Request)} if the municipalityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the municipalityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the municipalityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/municipalities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MunicipalityDTO> partialUpdateMunicipality(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MunicipalityDTO municipalityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Municipality partially : {}, {}", id, municipalityDTO);
        if (municipalityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, municipalityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!municipalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MunicipalityDTO> result = municipalityService.partialUpdate(municipalityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, municipalityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /municipalities} : get all the municipalities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of municipalities in body.
     */
    @GetMapping("/municipalities")
    public ResponseEntity<List<MunicipalityDTO>> getAllMunicipalities(
        MunicipalityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Municipalities by criteria: {}", criteria);
        Page<MunicipalityDTO> page = municipalityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /municipalities/count} : count all the municipalities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/municipalities/count")
    public ResponseEntity<Long> countMunicipalities(MunicipalityCriteria criteria) {
        log.debug("REST request to count Municipalities by criteria: {}", criteria);
        return ResponseEntity.ok().body(municipalityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /municipalities/:id} : get the "id" municipality.
     *
     * @param id the id of the municipalityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the municipalityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/municipalities/{id}")
    public ResponseEntity<MunicipalityDTO> getMunicipality(@PathVariable Long id) {
        log.debug("REST request to get Municipality : {}", id);
        Optional<MunicipalityDTO> municipalityDTO = municipalityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(municipalityDTO);
    }

    /**
     * {@code DELETE  /municipalities/:id} : delete the "id" municipality.
     *
     * @param id the id of the municipalityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/municipalities/{id}")
    public ResponseEntity<Void> deleteMunicipality(@PathVariable Long id) {
        log.debug("REST request to delete Municipality : {}", id);
        municipalityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
