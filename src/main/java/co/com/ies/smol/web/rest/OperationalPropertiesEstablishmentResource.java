package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.OperationalPropertiesEstablishmentRepository;
import co.com.ies.smol.service.OperationalPropertiesEstablishmentQueryService;
import co.com.ies.smol.service.OperationalPropertiesEstablishmentService;
import co.com.ies.smol.service.criteria.OperationalPropertiesEstablishmentCriteria;
import co.com.ies.smol.service.dto.OperationalPropertiesEstablishmentDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.OperationalPropertiesEstablishment}.
 */
@RestController
@RequestMapping("/api")
public class OperationalPropertiesEstablishmentResource {

    private final Logger log = LoggerFactory.getLogger(OperationalPropertiesEstablishmentResource.class);

    private static final String ENTITY_NAME = "operationalPropertiesEstablishment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperationalPropertiesEstablishmentService operationalPropertiesEstablishmentService;

    private final OperationalPropertiesEstablishmentRepository operationalPropertiesEstablishmentRepository;

    private final OperationalPropertiesEstablishmentQueryService operationalPropertiesEstablishmentQueryService;

    public OperationalPropertiesEstablishmentResource(
        OperationalPropertiesEstablishmentService operationalPropertiesEstablishmentService,
        OperationalPropertiesEstablishmentRepository operationalPropertiesEstablishmentRepository,
        OperationalPropertiesEstablishmentQueryService operationalPropertiesEstablishmentQueryService
    ) {
        this.operationalPropertiesEstablishmentService = operationalPropertiesEstablishmentService;
        this.operationalPropertiesEstablishmentRepository = operationalPropertiesEstablishmentRepository;
        this.operationalPropertiesEstablishmentQueryService = operationalPropertiesEstablishmentQueryService;
    }

    /**
     * {@code POST  /operational-properties-establishments} : Create a new operationalPropertiesEstablishment.
     *
     * @param operationalPropertiesEstablishmentDTO the operationalPropertiesEstablishmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operationalPropertiesEstablishmentDTO, or with status {@code 400 (Bad Request)} if the operationalPropertiesEstablishment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operational-properties-establishments")
    public ResponseEntity<OperationalPropertiesEstablishmentDTO> createOperationalPropertiesEstablishment(
        @Valid @RequestBody OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to save OperationalPropertiesEstablishment : {}", operationalPropertiesEstablishmentDTO);
        if (operationalPropertiesEstablishmentDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new operationalPropertiesEstablishment cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        OperationalPropertiesEstablishmentDTO result = operationalPropertiesEstablishmentService.save(
            operationalPropertiesEstablishmentDTO
        );
        return ResponseEntity
            .created(new URI("/api/operational-properties-establishments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operational-properties-establishments/:id} : Updates an existing operationalPropertiesEstablishment.
     *
     * @param id the id of the operationalPropertiesEstablishmentDTO to save.
     * @param operationalPropertiesEstablishmentDTO the operationalPropertiesEstablishmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operationalPropertiesEstablishmentDTO,
     * or with status {@code 400 (Bad Request)} if the operationalPropertiesEstablishmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operationalPropertiesEstablishmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operational-properties-establishments/{id}")
    public ResponseEntity<OperationalPropertiesEstablishmentDTO> updateOperationalPropertiesEstablishment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OperationalPropertiesEstablishment : {}, {}", id, operationalPropertiesEstablishmentDTO);
        if (operationalPropertiesEstablishmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operationalPropertiesEstablishmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operationalPropertiesEstablishmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OperationalPropertiesEstablishmentDTO result = operationalPropertiesEstablishmentService.update(
            operationalPropertiesEstablishmentDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    operationalPropertiesEstablishmentDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /operational-properties-establishments/:id} : Partial updates given fields of an existing operationalPropertiesEstablishment, field will ignore if it is null
     *
     * @param id the id of the operationalPropertiesEstablishmentDTO to save.
     * @param operationalPropertiesEstablishmentDTO the operationalPropertiesEstablishmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operationalPropertiesEstablishmentDTO,
     * or with status {@code 400 (Bad Request)} if the operationalPropertiesEstablishmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the operationalPropertiesEstablishmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the operationalPropertiesEstablishmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/operational-properties-establishments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OperationalPropertiesEstablishmentDTO> partialUpdateOperationalPropertiesEstablishment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update OperationalPropertiesEstablishment partially : {}, {}",
            id,
            operationalPropertiesEstablishmentDTO
        );
        if (operationalPropertiesEstablishmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operationalPropertiesEstablishmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operationalPropertiesEstablishmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OperationalPropertiesEstablishmentDTO> result = operationalPropertiesEstablishmentService.partialUpdate(
            operationalPropertiesEstablishmentDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operationalPropertiesEstablishmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /operational-properties-establishments} : get all the operationalPropertiesEstablishments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operationalPropertiesEstablishments in body.
     */
    @GetMapping("/operational-properties-establishments")
    public ResponseEntity<List<OperationalPropertiesEstablishmentDTO>> getAllOperationalPropertiesEstablishments(
        OperationalPropertiesEstablishmentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OperationalPropertiesEstablishments by criteria: {}", criteria);
        Page<OperationalPropertiesEstablishmentDTO> page = operationalPropertiesEstablishmentQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /operational-properties-establishments/count} : count all the operationalPropertiesEstablishments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/operational-properties-establishments/count")
    public ResponseEntity<Long> countOperationalPropertiesEstablishments(OperationalPropertiesEstablishmentCriteria criteria) {
        log.debug("REST request to count OperationalPropertiesEstablishments by criteria: {}", criteria);
        return ResponseEntity.ok().body(operationalPropertiesEstablishmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /operational-properties-establishments/:id} : get the "id" operationalPropertiesEstablishment.
     *
     * @param id the id of the operationalPropertiesEstablishmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operationalPropertiesEstablishmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operational-properties-establishments/{id}")
    public ResponseEntity<OperationalPropertiesEstablishmentDTO> getOperationalPropertiesEstablishment(@PathVariable Long id) {
        log.debug("REST request to get OperationalPropertiesEstablishment : {}", id);
        Optional<OperationalPropertiesEstablishmentDTO> operationalPropertiesEstablishmentDTO = operationalPropertiesEstablishmentService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(operationalPropertiesEstablishmentDTO);
    }

    /**
     * {@code DELETE  /operational-properties-establishments/:id} : delete the "id" operationalPropertiesEstablishment.
     *
     * @param id the id of the operationalPropertiesEstablishmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operational-properties-establishments/{id}")
    public ResponseEntity<Void> deleteOperationalPropertiesEstablishment(@PathVariable Long id) {
        log.debug("REST request to delete OperationalPropertiesEstablishment : {}", id);
        operationalPropertiesEstablishmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
