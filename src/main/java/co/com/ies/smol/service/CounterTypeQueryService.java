package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.CounterType;
import co.com.ies.smol.repository.CounterTypeRepository;
import co.com.ies.smol.service.criteria.CounterTypeCriteria;
import co.com.ies.smol.service.dto.CounterTypeDTO;
import co.com.ies.smol.service.mapper.CounterTypeMapper;
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
 * Service for executing complex queries for {@link CounterType} entities in the database.
 * The main input is a {@link CounterTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CounterTypeDTO} or a {@link Page} of {@link CounterTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CounterTypeQueryService extends QueryService<CounterType> {

    private final Logger log = LoggerFactory.getLogger(CounterTypeQueryService.class);

    private final CounterTypeRepository counterTypeRepository;

    private final CounterTypeMapper counterTypeMapper;

    public CounterTypeQueryService(CounterTypeRepository counterTypeRepository, CounterTypeMapper counterTypeMapper) {
        this.counterTypeRepository = counterTypeRepository;
        this.counterTypeMapper = counterTypeMapper;
    }

    /**
     * Return a {@link List} of {@link CounterTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CounterTypeDTO> findByCriteria(CounterTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CounterType> specification = createSpecification(criteria);
        return counterTypeMapper.toDto(counterTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CounterTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CounterTypeDTO> findByCriteria(CounterTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CounterType> specification = createSpecification(criteria);
        return counterTypeRepository.findAll(specification, page).map(counterTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CounterTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CounterType> specification = createSpecification(criteria);
        return counterTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CounterTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CounterType> createSpecification(CounterTypeCriteria criteria) {
        Specification<CounterType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getCounterCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCounterCode(), CounterType_.counterCode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CounterType_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CounterType_.description));
            }
            if (criteria.getIncludedInFormula() != null) {
                specification = specification.and(buildSpecification(criteria.getIncludedInFormula(), CounterType_.includedInFormula));
            }
            if (criteria.getPrize() != null) {
                specification = specification.and(buildSpecification(criteria.getPrize(), CounterType_.prize));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), CounterType_.category));
            }
            if (criteria.getUdteWaitTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUdteWaitTime(), CounterType_.udteWaitTime));
            }
        }
        return specification;
    }
}
