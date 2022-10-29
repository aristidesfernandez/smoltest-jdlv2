package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.CounterDeviceRepository;
import co.com.ies.smol.service.CounterDeviceQueryService;
import co.com.ies.smol.service.CounterDeviceService;
import co.com.ies.smol.service.criteria.CounterDeviceCriteria;
import co.com.ies.smol.service.dto.CounterDeviceDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.CounterDevice}.
 */
@RestController
@RequestMapping("/api")
public class CounterDeviceResource {

    private final Logger log = LoggerFactory.getLogger(CounterDeviceResource.class);

    private static final String ENTITY_NAME = "counterDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CounterDeviceService counterDeviceService;

    private final CounterDeviceRepository counterDeviceRepository;

    private final CounterDeviceQueryService counterDeviceQueryService;

    public CounterDeviceResource(
        CounterDeviceService counterDeviceService,
        CounterDeviceRepository counterDeviceRepository,
        CounterDeviceQueryService counterDeviceQueryService
    ) {
        this.counterDeviceService = counterDeviceService;
        this.counterDeviceRepository = counterDeviceRepository;
        this.counterDeviceQueryService = counterDeviceQueryService;
    }

    /**
     * {@code POST  /counter-devices} : Create a new counterDevice.
     *
     * @param counterDeviceDTO the counterDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new counterDeviceDTO, or with status {@code 400 (Bad Request)} if the counterDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/counter-devices")
    public ResponseEntity<CounterDeviceDTO> createCounterDevice(@Valid @RequestBody CounterDeviceDTO counterDeviceDTO)
        throws URISyntaxException {
        log.debug("REST request to save CounterDevice : {}", counterDeviceDTO);
        if (counterDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new counterDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CounterDeviceDTO result = counterDeviceService.save(counterDeviceDTO);
        return ResponseEntity
            .created(new URI("/api/counter-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /counter-devices/:id} : Updates an existing counterDevice.
     *
     * @param id the id of the counterDeviceDTO to save.
     * @param counterDeviceDTO the counterDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the counterDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the counterDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/counter-devices/{id}")
    public ResponseEntity<CounterDeviceDTO> updateCounterDevice(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CounterDeviceDTO counterDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CounterDevice : {}, {}", id, counterDeviceDTO);
        if (counterDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CounterDeviceDTO result = counterDeviceService.update(counterDeviceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /counter-devices/:id} : Partial updates given fields of an existing counterDevice, field will ignore if it is null
     *
     * @param id the id of the counterDeviceDTO to save.
     * @param counterDeviceDTO the counterDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the counterDeviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the counterDeviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the counterDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/counter-devices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CounterDeviceDTO> partialUpdateCounterDevice(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CounterDeviceDTO counterDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CounterDevice partially : {}, {}", id, counterDeviceDTO);
        if (counterDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CounterDeviceDTO> result = counterDeviceService.partialUpdate(counterDeviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterDeviceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /counter-devices} : get all the counterDevices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counterDevices in body.
     */
    @GetMapping("/counter-devices")
    public ResponseEntity<List<CounterDeviceDTO>> getAllCounterDevices(
        CounterDeviceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CounterDevices by criteria: {}", criteria);
        Page<CounterDeviceDTO> page = counterDeviceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /counter-devices/count} : count all the counterDevices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/counter-devices/count")
    public ResponseEntity<Long> countCounterDevices(CounterDeviceCriteria criteria) {
        log.debug("REST request to count CounterDevices by criteria: {}", criteria);
        return ResponseEntity.ok().body(counterDeviceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /counter-devices/:id} : get the "id" counterDevice.
     *
     * @param id the id of the counterDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the counterDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/counter-devices/{id}")
    public ResponseEntity<CounterDeviceDTO> getCounterDevice(@PathVariable UUID id) {
        log.debug("REST request to get CounterDevice : {}", id);
        Optional<CounterDeviceDTO> counterDeviceDTO = counterDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(counterDeviceDTO);
    }

    /**
     * {@code DELETE  /counter-devices/:id} : delete the "id" counterDevice.
     *
     * @param id the id of the counterDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/counter-devices/{id}")
    public ResponseEntity<Void> deleteCounterDevice(@PathVariable UUID id) {
        log.debug("REST request to delete CounterDevice : {}", id);
        counterDeviceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
