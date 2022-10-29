package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.DeviceInterfaceRepository;
import co.com.ies.smol.service.DeviceInterfaceQueryService;
import co.com.ies.smol.service.DeviceInterfaceService;
import co.com.ies.smol.service.criteria.DeviceInterfaceCriteria;
import co.com.ies.smol.service.dto.DeviceInterfaceDTO;
import co.com.ies.smol.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link co.com.ies.smol.domain.DeviceInterface}.
 */
@RestController
@RequestMapping("/api")
public class DeviceInterfaceResource {

    private final Logger log = LoggerFactory.getLogger(DeviceInterfaceResource.class);

    private static final String ENTITY_NAME = "deviceInterface";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceInterfaceService deviceInterfaceService;

    private final DeviceInterfaceRepository deviceInterfaceRepository;

    private final DeviceInterfaceQueryService deviceInterfaceQueryService;

    public DeviceInterfaceResource(
        DeviceInterfaceService deviceInterfaceService,
        DeviceInterfaceRepository deviceInterfaceRepository,
        DeviceInterfaceQueryService deviceInterfaceQueryService
    ) {
        this.deviceInterfaceService = deviceInterfaceService;
        this.deviceInterfaceRepository = deviceInterfaceRepository;
        this.deviceInterfaceQueryService = deviceInterfaceQueryService;
    }

    /**
     * {@code POST  /device-interfaces} : Create a new deviceInterface.
     *
     * @param deviceInterfaceDTO the deviceInterfaceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceInterfaceDTO, or with status {@code 400 (Bad Request)} if the deviceInterface has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-interfaces")
    public ResponseEntity<DeviceInterfaceDTO> createDeviceInterface(@RequestBody DeviceInterfaceDTO deviceInterfaceDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeviceInterface : {}", deviceInterfaceDTO);
        if (deviceInterfaceDTO.getId() != null) {
            throw new BadRequestAlertException("A new deviceInterface cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceInterfaceDTO result = deviceInterfaceService.save(deviceInterfaceDTO);
        return ResponseEntity
            .created(new URI("/api/device-interfaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-interfaces/:id} : Updates an existing deviceInterface.
     *
     * @param id the id of the deviceInterfaceDTO to save.
     * @param deviceInterfaceDTO the deviceInterfaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceInterfaceDTO,
     * or with status {@code 400 (Bad Request)} if the deviceInterfaceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceInterfaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-interfaces/{id}")
    public ResponseEntity<DeviceInterfaceDTO> updateDeviceInterface(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeviceInterfaceDTO deviceInterfaceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeviceInterface : {}, {}", id, deviceInterfaceDTO);
        if (deviceInterfaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceInterfaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceInterfaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeviceInterfaceDTO result = deviceInterfaceService.update(deviceInterfaceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceInterfaceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /device-interfaces/:id} : Partial updates given fields of an existing deviceInterface, field will ignore if it is null
     *
     * @param id the id of the deviceInterfaceDTO to save.
     * @param deviceInterfaceDTO the deviceInterfaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceInterfaceDTO,
     * or with status {@code 400 (Bad Request)} if the deviceInterfaceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deviceInterfaceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deviceInterfaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/device-interfaces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeviceInterfaceDTO> partialUpdateDeviceInterface(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeviceInterfaceDTO deviceInterfaceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeviceInterface partially : {}, {}", id, deviceInterfaceDTO);
        if (deviceInterfaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceInterfaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceInterfaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeviceInterfaceDTO> result = deviceInterfaceService.partialUpdate(deviceInterfaceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceInterfaceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /device-interfaces} : get all the deviceInterfaces.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceInterfaces in body.
     */
    @GetMapping("/device-interfaces")
    public ResponseEntity<List<DeviceInterfaceDTO>> getAllDeviceInterfaces(
        DeviceInterfaceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DeviceInterfaces by criteria: {}", criteria);
        Page<DeviceInterfaceDTO> page = deviceInterfaceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /device-interfaces/count} : count all the deviceInterfaces.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/device-interfaces/count")
    public ResponseEntity<Long> countDeviceInterfaces(DeviceInterfaceCriteria criteria) {
        log.debug("REST request to count DeviceInterfaces by criteria: {}", criteria);
        return ResponseEntity.ok().body(deviceInterfaceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /device-interfaces/:id} : get the "id" deviceInterface.
     *
     * @param id the id of the deviceInterfaceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceInterfaceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-interfaces/{id}")
    public ResponseEntity<DeviceInterfaceDTO> getDeviceInterface(@PathVariable Long id) {
        log.debug("REST request to get DeviceInterface : {}", id);
        Optional<DeviceInterfaceDTO> deviceInterfaceDTO = deviceInterfaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceInterfaceDTO);
    }

    /**
     * {@code DELETE  /device-interfaces/:id} : delete the "id" deviceInterface.
     *
     * @param id the id of the deviceInterfaceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-interfaces/{id}")
    public ResponseEntity<Void> deleteDeviceInterface(@PathVariable Long id) {
        log.debug("REST request to delete DeviceInterface : {}", id);
        deviceInterfaceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
