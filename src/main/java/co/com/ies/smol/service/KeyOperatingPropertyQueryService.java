package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.KeyOperatingProperty;
import co.com.ies.smol.repository.KeyOperatingPropertyRepository;
import co.com.ies.smol.service.criteria.KeyOperatingPropertyCriteria;
import co.com.ies.smol.service.dto.KeyOperatingPropertyDTO;
import co.com.ies.smol.service.mapper.KeyOperatingPropertyMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link KeyOperatingProperty} entities in the database.
 * The main input is a {@link KeyOperatingPropertyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link KeyOperatingPropertyDTO} or a {@link Page} of {@link KeyOperatingPropertyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KeyOperatingPropertyQueryService extends QueryService<KeyOperatingProperty> {

    private final Logger log = LoggerFactory.getLogger(KeyOperatingPropertyQueryService.class);

    private final KeyOperatingPropertyRepository keyOperatingPropertyRepository;

    private final KeyOperatingPropertyMapper keyOperatingPropertyMapper;

    public KeyOperatingPropertyQueryService(
        KeyOperatingPropertyRepository keyOperatingPropertyRepository,
        KeyOperatingPropertyMapper keyOperatingPropertyMapper
    ) {
        this.keyOperatingPropertyRepository = keyOperatingPropertyRepository;
        this.keyOperatingPropertyMapper = keyOperatingPropertyMapper;
    }

    /**
     * Return a {@link List} of {@link KeyOperatingPropertyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<KeyOperatingPropertyDTO> findByCriteria(KeyOperatingPropertyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<KeyOperatingProperty> specification = createSpecification(criteria);
        return keyOperatingPropertyMapper.toDto(keyOperatingPropertyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link KeyOperatingPropertyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KeyOperatingPropertyDTO> findByCriteria(KeyOperatingPropertyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<KeyOperatingProperty> specification = createSpecification(criteria);
        return keyOperatingPropertyRepository.findAll(specification, page).map(keyOperatingPropertyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KeyOperatingPropertyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<KeyOperatingProperty> specification = createSpecification(criteria);
        return keyOperatingPropertyRepository.count(specification);
    }

    /**
     * Function to convert {@link KeyOperatingPropertyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<KeyOperatingProperty> createSpecification(KeyOperatingPropertyCriteria criteria) {
        Specification<KeyOperatingProperty> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), KeyOperatingProperty_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), KeyOperatingProperty_.description));
            }
            if (criteria.getProperty() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProperty(), KeyOperatingProperty_.property));
            }
        }
        return specification;
    }
}
