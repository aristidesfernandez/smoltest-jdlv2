package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.CounterEventRepository;
import co.com.ies.smol.service.CounterEventQueryService;
import co.com.ies.smol.service.CounterEventService;
import co.com.ies.smol.service.criteria.CounterEventCriteria;
import co.com.ies.smol.service.dto.CounterEventDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.CounterEvent}.
 */
@RestController
@RequestMapping("/api")
public class CounterEventResource {

    private final Logger log = LoggerFactory.getLogger(CounterEventResource.class);

    private static final String ENTITY_NAME = "counterEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CounterEventService counterEventService;

    private final CounterEventRepository counterEventRepository;

    private final CounterEventQueryService counterEventQueryService;

    public CounterEventResource(
        CounterEventService counterEventService,
        CounterEventRepository counterEventRepository,
        CounterEventQueryService counterEventQueryService
    ) {
        this.counterEventService = counterEventService;
        this.counterEventRepository = counterEventRepository;
        this.counterEventQueryService = counterEventQueryService;
    }

    /**
     * {@code POST  /counter-events} : Create a new counterEvent.
     *
     * @param counterEventDTO the counterEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new counterEventDTO, or with status {@code 400 (Bad Request)} if the counterEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/counter-events")
    public ResponseEntity<CounterEventDTO> createCounterEvent(@Valid @RequestBody CounterEventDTO counterEventDTO)
        throws URISyntaxException {
        log.debug("REST request to save CounterEvent : {}", counterEventDTO);
        if (counterEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new counterEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CounterEventDTO result = counterEventService.save(counterEventDTO);
        return ResponseEntity
            .created(new URI("/api/counter-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /counter-events/:id} : Updates an existing counterEvent.
     *
     * @param id the id of the counterEventDTO to save.
     * @param counterEventDTO the counterEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterEventDTO,
     * or with status {@code 400 (Bad Request)} if the counterEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the counterEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/counter-events/{id}")
    public ResponseEntity<CounterEventDTO> updateCounterEvent(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CounterEventDTO counterEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CounterEvent : {}, {}", id, counterEventDTO);
        if (counterEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CounterEventDTO result = counterEventService.update(counterEventDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /counter-events/:id} : Partial updates given fields of an existing counterEvent, field will ignore if it is null
     *
     * @param id the id of the counterEventDTO to save.
     * @param counterEventDTO the counterEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterEventDTO,
     * or with status {@code 400 (Bad Request)} if the counterEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the counterEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the counterEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/counter-events/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CounterEventDTO> partialUpdateCounterEvent(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CounterEventDTO counterEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CounterEvent partially : {}, {}", id, counterEventDTO);
        if (counterEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CounterEventDTO> result = counterEventService.partialUpdate(counterEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /counter-events} : get all the counterEvents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counterEvents in body.
     */
    @GetMapping("/counter-events")
    public ResponseEntity<List<CounterEventDTO>> getAllCounterEvents(
        CounterEventCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CounterEvents by criteria: {}", criteria);
        Page<CounterEventDTO> page = counterEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /counter-events/count} : count all the counterEvents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/counter-events/count")
    public ResponseEntity<Long> countCounterEvents(CounterEventCriteria criteria) {
        log.debug("REST request to count CounterEvents by criteria: {}", criteria);
        return ResponseEntity.ok().body(counterEventQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /counter-events/:id} : get the "id" counterEvent.
     *
     * @param id the id of the counterEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the counterEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/counter-events/{id}")
    public ResponseEntity<CounterEventDTO> getCounterEvent(@PathVariable UUID id) {
        log.debug("REST request to get CounterEvent : {}", id);
        Optional<CounterEventDTO> counterEventDTO = counterEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(counterEventDTO);
    }

    /**
     * {@code DELETE  /counter-events/:id} : delete the "id" counterEvent.
     *
     * @param id the id of the counterEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/counter-events/{id}")
    public ResponseEntity<Void> deleteCounterEvent(@PathVariable UUID id) {
        log.debug("REST request to delete CounterEvent : {}", id);
        counterEventService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
