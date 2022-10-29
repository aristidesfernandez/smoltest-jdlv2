package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.EstablishmentRepository;
import co.com.ies.smol.service.EstablishmentQueryService;
import co.com.ies.smol.service.EstablishmentService;
import co.com.ies.smol.service.criteria.EstablishmentCriteria;
import co.com.ies.smol.service.dto.EstablishmentDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.Establishment}.
 */
@RestController
@RequestMapping("/api")
public class EstablishmentResource {

    private final Logger log = LoggerFactory.getLogger(EstablishmentResource.class);

    private static final String ENTITY_NAME = "establishment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstablishmentService establishmentService;

    private final EstablishmentRepository establishmentRepository;

    private final EstablishmentQueryService establishmentQueryService;

    public EstablishmentResource(
        EstablishmentService establishmentService,
        EstablishmentRepository establishmentRepository,
        EstablishmentQueryService establishmentQueryService
    ) {
        this.establishmentService = establishmentService;
        this.establishmentRepository = establishmentRepository;
        this.establishmentQueryService = establishmentQueryService;
    }

    /**
     * {@code POST  /establishments} : Create a new establishment.
     *
     * @param establishmentDTO the establishmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new establishmentDTO, or with status {@code 400 (Bad Request)} if the establishment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/establishments")
    public ResponseEntity<EstablishmentDTO> createEstablishment(@Valid @RequestBody EstablishmentDTO establishmentDTO)
        throws URISyntaxException {
        log.debug("REST request to save Establishment : {}", establishmentDTO);
        if (establishmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new establishment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EstablishmentDTO result = establishmentService.save(establishmentDTO);
        return ResponseEntity
            .created(new URI("/api/establishments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /establishments/:id} : Updates an existing establishment.
     *
     * @param id the id of the establishmentDTO to save.
     * @param establishmentDTO the establishmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated establishmentDTO,
     * or with status {@code 400 (Bad Request)} if the establishmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the establishmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/establishments/{id}")
    public ResponseEntity<EstablishmentDTO> updateEstablishment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EstablishmentDTO establishmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Establishment : {}, {}", id, establishmentDTO);
        if (establishmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, establishmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!establishmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EstablishmentDTO result = establishmentService.update(establishmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, establishmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /establishments/:id} : Partial updates given fields of an existing establishment, field will ignore if it is null
     *
     * @param id the id of the establishmentDTO to save.
     * @param establishmentDTO the establishmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated establishmentDTO,
     * or with status {@code 400 (Bad Request)} if the establishmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the establishmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the establishmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/establishments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstablishmentDTO> partialUpdateEstablishment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EstablishmentDTO establishmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Establishment partially : {}, {}", id, establishmentDTO);
        if (establishmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, establishmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!establishmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstablishmentDTO> result = establishmentService.partialUpdate(establishmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, establishmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /establishments} : get all the establishments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of establishments in body.
     */
    @GetMapping("/establishments")
    public ResponseEntity<List<EstablishmentDTO>> getAllEstablishments(
        EstablishmentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Establishments by criteria: {}", criteria);
        Page<EstablishmentDTO> page = establishmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /establishments/count} : count all the establishments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/establishments/count")
    public ResponseEntity<Long> countEstablishments(EstablishmentCriteria criteria) {
        log.debug("REST request to count Establishments by criteria: {}", criteria);
        return ResponseEntity.ok().body(establishmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /establishments/:id} : get the "id" establishment.
     *
     * @param id the id of the establishmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the establishmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/establishments/{id}")
    public ResponseEntity<EstablishmentDTO> getEstablishment(@PathVariable Long id) {
        log.debug("REST request to get Establishment : {}", id);
        Optional<EstablishmentDTO> establishmentDTO = establishmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(establishmentDTO);
    }

    /**
     * {@code DELETE  /establishments/:id} : delete the "id" establishment.
     *
     * @param id the id of the establishmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/establishments/{id}")
    public ResponseEntity<Void> deleteEstablishment(@PathVariable Long id) {
        log.debug("REST request to delete Establishment : {}", id);
        establishmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
