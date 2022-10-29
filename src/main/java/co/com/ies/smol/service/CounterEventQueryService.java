package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.CounterEvent;
import co.com.ies.smol.repository.CounterEventRepository;
import co.com.ies.smol.service.criteria.CounterEventCriteria;
import co.com.ies.smol.service.dto.CounterEventDTO;
import co.com.ies.smol.service.mapper.CounterEventMapper;
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
 * Service for executing complex queries for {@link CounterEvent} entities in the database.
 * The main input is a {@link CounterEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CounterEventDTO} or a {@link Page} of {@link CounterEventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CounterEventQueryService extends QueryService<CounterEvent> {

    private final Logger log = LoggerFactory.getLogger(CounterEventQueryService.class);

    private final CounterEventRepository counterEventRepository;

    private final CounterEventMapper counterEventMapper;

    public CounterEventQueryService(CounterEventRepository counterEventRepository, CounterEventMapper counterEventMapper) {
        this.counterEventRepository = counterEventRepository;
        this.counterEventMapper = counterEventMapper;
    }

    /**
     * Return a {@link List} of {@link CounterEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CounterEventDTO> findByCriteria(CounterEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CounterEvent> specification = createSpecification(criteria);
        return counterEventMapper.toDto(counterEventRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CounterEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CounterEventDTO> findByCriteria(CounterEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CounterEvent> specification = createSpecification(criteria);
        return counterEventRepository.findAll(specification, page).map(counterEventMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CounterEventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CounterEvent> specification = createSpecification(criteria);
        return counterEventRepository.count(specification);
    }

    /**
     * Function to convert {@link CounterEventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CounterEvent> createSpecification(CounterEventCriteria criteria) {
        Specification<CounterEvent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CounterEvent_.id));
            }
            if (criteria.getValueCounter() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValueCounter(), CounterEvent_.valueCounter));
            }
            if (criteria.getDenominationSale() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDenominationSale(), CounterEvent_.denominationSale));
            }
            if (criteria.getCounterCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCounterCode(), CounterEvent_.counterCode));
            }
            if (criteria.getEventDeviceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventDeviceId(),
                            root -> root.join(CounterEvent_.eventDevice, JoinType.LEFT).get(EventDevice_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
