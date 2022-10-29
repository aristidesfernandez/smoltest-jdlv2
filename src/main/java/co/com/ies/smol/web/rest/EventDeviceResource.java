package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.EventDeviceRepository;
import co.com.ies.smol.service.EventDeviceQueryService;
import co.com.ies.smol.service.EventDeviceService;
import co.com.ies.smol.service.criteria.EventDeviceCriteria;
import co.com.ies.smol.service.dto.EventDeviceDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.EventDevice}.
 */
@RestController
@RequestMapping("/api")
public class EventDeviceResource {

    private final Logger log = LoggerFactory.getLogger(EventDeviceResource.class);

    private static final String ENTITY_NAME = "eventDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventDeviceService eventDeviceService;

    private final EventDeviceRepository eventDeviceRepository;

    private final EventDeviceQueryService eventDeviceQueryService;

    public EventDeviceResource(
        EventDeviceService eventDeviceService,
        EventDeviceRepository eventDeviceRepository,
        EventDeviceQueryService eventDeviceQueryService
    ) {
        this.eventDeviceService = eventDeviceService;
        this.eventDeviceRepository = eventDeviceRepository;
        this.eventDeviceQueryService = eventDeviceQueryService;
    }

    /**
     * {@code POST  /event-devices} : Create a new eventDevice.
     *
     * @param eventDeviceDTO the eventDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventDeviceDTO, or with status {@code 400 (Bad Request)} if the eventDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-devices")
    public ResponseEntity<EventDeviceDTO> createEventDevice(@Valid @RequestBody EventDeviceDTO eventDeviceDTO) throws URISyntaxException {
        log.debug("REST request to save EventDevice : {}", eventDeviceDTO);
        if (eventDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventDeviceDTO result = eventDeviceService.save(eventDeviceDTO);
        return ResponseEntity
            .created(new URI("/api/event-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-devices/:id} : Updates an existing eventDevice.
     *
     * @param id the id of the eventDeviceDTO to save.
     * @param eventDeviceDTO the eventDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the eventDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-devices/{id}")
    public ResponseEntity<EventDeviceDTO> updateEventDevice(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody EventDeviceDTO eventDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventDevice : {}, {}", id, eventDeviceDTO);
        if (eventDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventDeviceDTO result = eventDeviceService.update(eventDeviceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-devices/:id} : Partial updates given fields of an existing eventDevice, field will ignore if it is null
     *
     * @param id the id of the eventDeviceDTO to save.
     * @param eventDeviceDTO the eventDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the eventDeviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventDeviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-devices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventDeviceDTO> partialUpdateEventDevice(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody EventDeviceDTO eventDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventDevice partially : {}, {}", id, eventDeviceDTO);
        if (eventDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventDeviceDTO> result = eventDeviceService.partialUpdate(eventDeviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventDeviceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-devices} : get all the eventDevices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventDevices in body.
     */
    @GetMapping("/event-devices")
    public ResponseEntity<List<EventDeviceDTO>> getAllEventDevices(
        EventDeviceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventDevices by criteria: {}", criteria);
        Page<EventDeviceDTO> page = eventDeviceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-devices/count} : count all the eventDevices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/event-devices/count")
    public ResponseEntity<Long> countEventDevices(EventDeviceCriteria criteria) {
        log.debug("REST request to count EventDevices by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventDeviceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-devices/:id} : get the "id" eventDevice.
     *
     * @param id the id of the eventDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-devices/{id}")
    public ResponseEntity<EventDeviceDTO> getEventDevice(@PathVariable UUID id) {
        log.debug("REST request to get EventDevice : {}", id);
        Optional<EventDeviceDTO> eventDeviceDTO = eventDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventDeviceDTO);
    }

    /**
     * {@code DELETE  /event-devices/:id} : delete the "id" eventDevice.
     *
     * @param id the id of the eventDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-devices/{id}")
    public ResponseEntity<Void> deleteEventDevice(@PathVariable UUID id) {
        log.debug("REST request to delete EventDevice : {}", id);
        eventDeviceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
