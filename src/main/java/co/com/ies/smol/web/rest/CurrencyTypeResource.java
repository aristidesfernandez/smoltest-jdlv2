package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.CurrencyTypeRepository;
import co.com.ies.smol.service.CurrencyTypeQueryService;
import co.com.ies.smol.service.CurrencyTypeService;
import co.com.ies.smol.service.criteria.CurrencyTypeCriteria;
import co.com.ies.smol.service.dto.CurrencyTypeDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.CurrencyType}.
 */
@RestController
@RequestMapping("/api")
public class CurrencyTypeResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyTypeResource.class);

    private static final String ENTITY_NAME = "currencyType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyTypeService currencyTypeService;

    private final CurrencyTypeRepository currencyTypeRepository;

    private final CurrencyTypeQueryService currencyTypeQueryService;

    public CurrencyTypeResource(
        CurrencyTypeService currencyTypeService,
        CurrencyTypeRepository currencyTypeRepository,
        CurrencyTypeQueryService currencyTypeQueryService
    ) {
        this.currencyTypeService = currencyTypeService;
        this.currencyTypeRepository = currencyTypeRepository;
        this.currencyTypeQueryService = currencyTypeQueryService;
    }

    /**
     * {@code POST  /currency-types} : Create a new currencyType.
     *
     * @param currencyTypeDTO the currencyTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyTypeDTO, or with status {@code 400 (Bad Request)} if the currencyType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currency-types")
    public ResponseEntity<CurrencyTypeDTO> createCurrencyType(@Valid @RequestBody CurrencyTypeDTO currencyTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CurrencyType : {}", currencyTypeDTO);
        if (currencyTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new currencyType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyTypeDTO result = currencyTypeService.save(currencyTypeDTO);
        return ResponseEntity
            .created(new URI("/api/currency-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /currency-types/:id} : Updates an existing currencyType.
     *
     * @param id the id of the currencyTypeDTO to save.
     * @param currencyTypeDTO the currencyTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyTypeDTO,
     * or with status {@code 400 (Bad Request)} if the currencyTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currency-types/{id}")
    public ResponseEntity<CurrencyTypeDTO> updateCurrencyType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurrencyTypeDTO currencyTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CurrencyType : {}, {}", id, currencyTypeDTO);
        if (currencyTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CurrencyTypeDTO result = currencyTypeService.update(currencyTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /currency-types/:id} : Partial updates given fields of an existing currencyType, field will ignore if it is null
     *
     * @param id the id of the currencyTypeDTO to save.
     * @param currencyTypeDTO the currencyTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyTypeDTO,
     * or with status {@code 400 (Bad Request)} if the currencyTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the currencyTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the currencyTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/currency-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurrencyTypeDTO> partialUpdateCurrencyType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurrencyTypeDTO currencyTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CurrencyType partially : {}, {}", id, currencyTypeDTO);
        if (currencyTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurrencyTypeDTO> result = currencyTypeService.partialUpdate(currencyTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /currency-types} : get all the currencyTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencyTypes in body.
     */
    @GetMapping("/currency-types")
    public ResponseEntity<List<CurrencyTypeDTO>> getAllCurrencyTypes(
        CurrencyTypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CurrencyTypes by criteria: {}", criteria);
        Page<CurrencyTypeDTO> page = currencyTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /currency-types/count} : count all the currencyTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/currency-types/count")
    public ResponseEntity<Long> countCurrencyTypes(CurrencyTypeCriteria criteria) {
        log.debug("REST request to count CurrencyTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(currencyTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /currency-types/:id} : get the "id" currencyType.
     *
     * @param id the id of the currencyTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currency-types/{id}")
    public ResponseEntity<CurrencyTypeDTO> getCurrencyType(@PathVariable Long id) {
        log.debug("REST request to get CurrencyType : {}", id);
        Optional<CurrencyTypeDTO> currencyTypeDTO = currencyTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyTypeDTO);
    }

    /**
     * {@code DELETE  /currency-types/:id} : delete the "id" currencyType.
     *
     * @param id the id of the currencyTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currency-types/{id}")
    public ResponseEntity<Void> deleteCurrencyType(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyType : {}", id);
        currencyTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
