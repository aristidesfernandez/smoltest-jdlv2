package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.CounterDevice;
import co.com.ies.smol.repository.CounterDeviceRepository;
import co.com.ies.smol.service.criteria.CounterDeviceCriteria;
import co.com.ies.smol.service.dto.CounterDeviceDTO;
import co.com.ies.smol.service.mapper.CounterDeviceMapper;
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
 * Service for executing complex queries for {@link CounterDevice} entities in the database.
 * The main input is a {@link CounterDeviceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CounterDeviceDTO} or a {@link Page} of {@link CounterDeviceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CounterDeviceQueryService extends QueryService<CounterDevice> {

    private final Logger log = LoggerFactory.getLogger(CounterDeviceQueryService.class);

    private final CounterDeviceRepository counterDeviceRepository;

    private final CounterDeviceMapper counterDeviceMapper;

    public CounterDeviceQueryService(CounterDeviceRepository counterDeviceRepository, CounterDeviceMapper counterDeviceMapper) {
        this.counterDeviceRepository = counterDeviceRepository;
        this.counterDeviceMapper = counterDeviceMapper;
    }

    /**
     * Return a {@link List} of {@link CounterDeviceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CounterDeviceDTO> findByCriteria(CounterDeviceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CounterDevice> specification = createSpecification(criteria);
        return counterDeviceMapper.toDto(counterDeviceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CounterDeviceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CounterDeviceDTO> findByCriteria(CounterDeviceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CounterDevice> specification = createSpecification(criteria);
        return counterDeviceRepository.findAll(specification, page).map(counterDeviceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CounterDeviceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CounterDevice> specification = createSpecification(criteria);
        return counterDeviceRepository.count(specification);
    }

    /**
     * Function to convert {@link CounterDeviceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CounterDevice> createSpecification(CounterDeviceCriteria criteria) {
        Specification<CounterDevice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CounterDevice_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), CounterDevice_.value));
            }
            if (criteria.getRolloverValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRolloverValue(), CounterDevice_.rolloverValue));
            }
            if (criteria.getCreditSale() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditSale(), CounterDevice_.creditSale));
            }
            if (criteria.getManualCounter() != null) {
                specification = specification.and(buildSpecification(criteria.getManualCounter(), CounterDevice_.manualCounter));
            }
            if (criteria.getManualMultiplier() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getManualMultiplier(), CounterDevice_.manualMultiplier));
            }
            if (criteria.getDecimalsManualCounter() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getDecimalsManualCounter(), CounterDevice_.decimalsManualCounter));
            }
            if (criteria.getDeviceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDeviceId(), root -> root.join(CounterDevice_.device, JoinType.LEFT).get(Device_.id))
                    );
            }
        }
        return specification;
    }
}
