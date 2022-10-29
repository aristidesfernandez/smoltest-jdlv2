package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.CounterTypeRepository;
import co.com.ies.smol.service.CounterTypeQueryService;
import co.com.ies.smol.service.CounterTypeService;
import co.com.ies.smol.service.criteria.CounterTypeCriteria;
import co.com.ies.smol.service.dto.CounterTypeDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.CounterType}.
 */
@RestController
@RequestMapping("/api")
public class CounterTypeResource {

    private final Logger log = LoggerFactory.getLogger(CounterTypeResource.class);

    private static final String ENTITY_NAME = "counterType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CounterTypeService counterTypeService;

    private final CounterTypeRepository counterTypeRepository;

    private final CounterTypeQueryService counterTypeQueryService;

    public CounterTypeResource(
        CounterTypeService counterTypeService,
        CounterTypeRepository counterTypeRepository,
        CounterTypeQueryService counterTypeQueryService
    ) {
        this.counterTypeService = counterTypeService;
        this.counterTypeRepository = counterTypeRepository;
        this.counterTypeQueryService = counterTypeQueryService;
    }

    /**
     * {@code POST  /counter-types} : Create a new counterType.
     *
     * @param counterTypeDTO the counterTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new counterTypeDTO, or with status {@code 400 (Bad Request)} if the counterType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/counter-types")
    public ResponseEntity<CounterTypeDTO> createCounterType(@Valid @RequestBody CounterTypeDTO counterTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CounterType : {}", counterTypeDTO);
        if (counterTypeDTO.getCounterCode() != null) {
            throw new BadRequestAlertException("A new counterType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CounterTypeDTO result = counterTypeService.save(counterTypeDTO);
        return ResponseEntity
            .created(new URI("/api/counter-types/" + result.getCounterCode()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getCounterCode()))
            .body(result);
    }

    /**
     * {@code PUT  /counter-types/:counterCode} : Updates an existing counterType.
     *
     * @param counterCode the id of the counterTypeDTO to save.
     * @param counterTypeDTO the counterTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterTypeDTO,
     * or with status {@code 400 (Bad Request)} if the counterTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the counterTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/counter-types/{counterCode}")
    public ResponseEntity<CounterTypeDTO> updateCounterType(
        @PathVariable(value = "counterCode", required = false) final String counterCode,
        @Valid @RequestBody CounterTypeDTO counterTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CounterType : {}, {}", counterCode, counterTypeDTO);
        if (counterTypeDTO.getCounterCode() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(counterCode, counterTypeDTO.getCounterCode())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterTypeRepository.existsById(counterCode)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CounterTypeDTO result = counterTypeService.update(counterTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterTypeDTO.getCounterCode()))
            .body(result);
    }

    /**
     * {@code PATCH  /counter-types/:counterCode} : Partial updates given fields of an existing counterType, field will ignore if it is null
     *
     * @param counterCode the id of the counterTypeDTO to save.
     * @param counterTypeDTO the counterTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterTypeDTO,
     * or with status {@code 400 (Bad Request)} if the counterTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the counterTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the counterTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/counter-types/{counterCode}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CounterTypeDTO> partialUpdateCounterType(
        @PathVariable(value = "counterCode", required = false) final String counterCode,
        @NotNull @RequestBody CounterTypeDTO counterTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CounterType partially : {}, {}", counterCode, counterTypeDTO);
        if (counterTypeDTO.getCounterCode() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(counterCode, counterTypeDTO.getCounterCode())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterTypeRepository.existsById(counterCode)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CounterTypeDTO> result = counterTypeService.partialUpdate(counterTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterTypeDTO.getCounterCode())
        );
    }

    /**
     * {@code GET  /counter-types} : get all the counterTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counterTypes in body.
     */
    @GetMapping("/counter-types")
    public ResponseEntity<List<CounterTypeDTO>> getAllCounterTypes(
        CounterTypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CounterTypes by criteria: {}", criteria);
        Page<CounterTypeDTO> page = counterTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /counter-types/count} : count all the counterTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/counter-types/count")
    public ResponseEntity<Long> countCounterTypes(CounterTypeCriteria criteria) {
        log.debug("REST request to count CounterTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(counterTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /counter-types/:id} : get the "id" counterType.
     *
     * @param id the id of the counterTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the counterTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/counter-types/{id}")
    public ResponseEntity<CounterTypeDTO> getCounterType(@PathVariable String id) {
        log.debug("REST request to get CounterType : {}", id);
        Optional<CounterTypeDTO> counterTypeDTO = counterTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(counterTypeDTO);
    }

    /**
     * {@code DELETE  /counter-types/:id} : delete the "id" counterType.
     *
     * @param id the id of the counterTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/counter-types/{id}")
    public ResponseEntity<Void> deleteCounterType(@PathVariable String id) {
        log.debug("REST request to delete CounterType : {}", id);
        counterTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
