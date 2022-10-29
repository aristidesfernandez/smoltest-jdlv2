package co.com.ies.smol.web.rest;

import co.com.ies.smol.repository.IsleRepository;
import co.com.ies.smol.service.IsleQueryService;
import co.com.ies.smol.service.IsleService;
import co.com.ies.smol.service.criteria.IsleCriteria;
import co.com.ies.smol.service.dto.IsleDTO;
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
 * REST controller for managing {@link co.com.ies.smol.domain.Isle}.
 */
@RestController
@RequestMapping("/api")
public class IsleResource {

    private final Logger log = LoggerFactory.getLogger(IsleResource.class);

    private static final String ENTITY_NAME = "isle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IsleService isleService;

    private final IsleRepository isleRepository;

    private final IsleQueryService isleQueryService;

    public IsleResource(IsleService isleService, IsleRepository isleRepository, IsleQueryService isleQueryService) {
        this.isleService = isleService;
        this.isleRepository = isleRepository;
        this.isleQueryService = isleQueryService;
    }

    /**
     * {@code POST  /isles} : Create a new isle.
     *
     * @param isleDTO the isleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new isleDTO, or with status {@code 400 (Bad Request)} if the isle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/isles")
    public ResponseEntity<IsleDTO> createIsle(@Valid @RequestBody IsleDTO isleDTO) throws URISyntaxException {
        log.debug("REST request to save Isle : {}", isleDTO);
        if (isleDTO.getId() != null) {
            throw new BadRequestAlertException("A new isle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IsleDTO result = isleService.save(isleDTO);
        return ResponseEntity
            .created(new URI("/api/isles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /isles/:id} : Updates an existing isle.
     *
     * @param id the id of the isleDTO to save.
     * @param isleDTO the isleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isleDTO,
     * or with status {@code 400 (Bad Request)} if the isleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the isleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/isles/{id}")
    public ResponseEntity<IsleDTO> updateIsle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IsleDTO isleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Isle : {}, {}", id, isleDTO);
        if (isleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IsleDTO result = isleService.update(isleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /isles/:id} : Partial updates given fields of an existing isle, field will ignore if it is null
     *
     * @param id the id of the isleDTO to save.
     * @param isleDTO the isleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isleDTO,
     * or with status {@code 400 (Bad Request)} if the isleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the isleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the isleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/isles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IsleDTO> partialUpdateIsle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IsleDTO isleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Isle partially : {}, {}", id, isleDTO);
        if (isleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IsleDTO> result = isleService.partialUpdate(isleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /isles} : get all the isles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of isles in body.
     */
    @GetMapping("/isles")
    public ResponseEntity<List<IsleDTO>> getAllIsles(
        IsleCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Isles by criteria: {}", criteria);
        Page<IsleDTO> page = isleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /isles/count} : count all the isles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/isles/count")
    public ResponseEntity<Long> countIsles(IsleCriteria criteria) {
        log.debug("REST request to count Isles by criteria: {}", criteria);
        return ResponseEntity.ok().body(isleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /isles/:id} : get the "id" isle.
     *
     * @param id the id of the isleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the isleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/isles/{id}")
    public ResponseEntity<IsleDTO> getIsle(@PathVariable Long id) {
        log.debug("REST request to get Isle : {}", id);
        Optional<IsleDTO> isleDTO = isleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(isleDTO);
    }

    /**
     * {@code DELETE  /isles/:id} : delete the "id" isle.
     *
     * @param id the id of the isleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/isles/{id}")
    public ResponseEntity<Void> deleteIsle(@PathVariable Long id) {
        log.debug("REST request to delete Isle : {}", id);
        isleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
