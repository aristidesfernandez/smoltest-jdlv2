package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.DeviceEstablishmentRepository;
import co.com.ies.smol.service.DeviceEstablishmentQueryService;
import co.com.ies.smol.service.DeviceEstablishmentService;
import co.com.ies.smol.service.criteria.DeviceEstablishmentCriteria;
import co.com.ies.smol.service.dto.DeviceEstablishmentDTO;
import co.com.ies.smol.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link co.com.ies.smol.domain.DeviceEstablishment}.
 */
@RestController
@RequestMapping("/api")
public class DeviceEstablishmentResource {

    private final Logger log = LoggerFactory.getLogger(DeviceEstablishmentResource.class);

    private static final String ENTITY_NAME = "deviceEstablishment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceEstablishmentService deviceEstablishmentService;

    private final DeviceEstablishmentRepository deviceEstablishmentRepository;

    private final DeviceEstablishmentQueryService deviceEstablishmentQueryService;

    public DeviceEstablishmentResource(
        DeviceEstablishmentService deviceEstablishmentService,
        DeviceEstablishmentRepository deviceEstablishmentRepository,
        DeviceEstablishmentQueryService deviceEstablishmentQueryService
    ) {
        this.deviceEstablishmentService = deviceEstablishmentService;
        this.deviceEstablishmentRepository = deviceEstablishmentRepository;
        this.deviceEstablishmentQueryService = deviceEstablishmentQueryService;
    }

    /**
     * {@code POST  /device-establishments} : Create a new deviceEstablishment.
     *
     * @param deviceEstablishmentDTO the deviceEstablishmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceEstablishmentDTO, or with status {@code 400 (Bad Request)} if the deviceEstablishment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-establishments")
    public ResponseEntity<DeviceEstablishmentDTO> createDeviceEstablishment(
        @Valid @RequestBody DeviceEstablishmentDTO deviceEstablishmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DeviceEstablishment : {}", deviceEstablishmentDTO);
        if (deviceEstablishmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new deviceEstablishment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceEstablishmentDTO result = deviceEstablishmentService.save(deviceEstablishmentDTO);
        return ResponseEntity
            .created(new URI("/api/device-establishments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-establishments/:id} : Updates an existing deviceEstablishment.
     *
     * @param id the id of the deviceEstablishmentDTO to save.
     * @param deviceEstablishmentDTO the deviceEstablishmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceEstablishmentDTO,
     * or with status {@code 400 (Bad Request)} if the deviceEstablishmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceEstablishmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-establishments/{id}")
    public ResponseEntity<DeviceEstablishmentDTO> updateDeviceEstablishment(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody DeviceEstablishmentDTO deviceEstablishmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeviceEstablishment : {}, {}", id, deviceEstablishmentDTO);
        if (deviceEstablishmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceEstablishmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceEstablishmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeviceEstablishmentDTO result = deviceEstablishmentService.update(deviceEstablishmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceEstablishmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /device-establishments/:id} : Partial updates given fields of an existing deviceEstablishment, field will ignore if it is null
     *
     * @param id the id of the deviceEstablishmentDTO to save.
     * @param deviceEstablishmentDTO the deviceEstablishmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceEstablishmentDTO,
     * or with status {@code 400 (Bad Request)} if the deviceEstablishmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deviceEstablishmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deviceEstablishmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/device-establishments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeviceEstablishmentDTO> partialUpdateDeviceEstablishment(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody DeviceEstablishmentDTO deviceEstablishmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeviceEstablishment partially : {}, {}", id, deviceEstablishmentDTO);
        if (deviceEstablishmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceEstablishmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceEstablishmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeviceEstablishmentDTO> result = deviceEstablishmentService.partialUpdate(deviceEstablishmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceEstablishmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /device-establishments} : get all the deviceEstablishments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceEstablishments in body.
     */
    @GetMapping("/device-establishments")
    public ResponseEntity<List<DeviceEstablishmentDTO>> getAllDeviceEstablishments(
        DeviceEstablishmentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DeviceEstablishments by criteria: {}", criteria);
        Page<DeviceEstablishmentDTO> page = deviceEstablishmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /device-establishments/count} : count all the deviceEstablishments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/device-establishments/count")
    public ResponseEntity<Long> countDeviceEstablishments(DeviceEstablishmentCriteria criteria) {
        log.debug("REST request to count DeviceEstablishments by criteria: {}", criteria);
        return ResponseEntity.ok().body(deviceEstablishmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /device-establishments/:id} : get the "id" deviceEstablishment.
     *
     * @param id the id of the deviceEstablishmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceEstablishmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-establishments/{id}")
    public ResponseEntity<DeviceEstablishmentDTO> getDeviceEstablishment(@PathVariable UUID id) {
        log.debug("REST request to get DeviceEstablishment : {}", id);
        Optional<DeviceEstablishmentDTO> deviceEstablishmentDTO = deviceEstablishmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceEstablishmentDTO);
    }

    /**
     * {@code DELETE  /device-establishments/:id} : delete the "id" deviceEstablishment.
     *
     * @param id the id of the deviceEstablishmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-establishments/{id}")
    public ResponseEntity<Void> deleteDeviceEstablishment(@PathVariable UUID id) {
        log.debug("REST request to delete DeviceEstablishment : {}", id);
        deviceEstablishmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
