package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.EventDevice;
import co.com.ies.smol.repository.EventDeviceRepository;
import co.com.ies.smol.service.criteria.EventDeviceCriteria;
import co.com.ies.smol.service.dto.EventDeviceDTO;
import co.com.ies.smol.service.mapper.EventDeviceMapper;
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
 * Service for executing complex queries for {@link EventDevice} entities in the database.
 * The main input is a {@link EventDeviceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventDeviceDTO} or a {@link Page} of {@link EventDeviceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventDeviceQueryService extends QueryService<EventDevice> {

    private final Logger log = LoggerFactory.getLogger(EventDeviceQueryService.class);

    private final EventDeviceRepository eventDeviceRepository;

    private final EventDeviceMapper eventDeviceMapper;

    public EventDeviceQueryService(EventDeviceRepository eventDeviceRepository, EventDeviceMapper eventDeviceMapper) {
        this.eventDeviceRepository = eventDeviceRepository;
        this.eventDeviceMapper = eventDeviceMapper;
    }

    /**
     * Return a {@link List} of {@link EventDeviceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventDeviceDTO> findByCriteria(EventDeviceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventDevice> specification = createSpecification(criteria);
        return eventDeviceMapper.toDto(eventDeviceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventDeviceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventDeviceDTO> findByCriteria(EventDeviceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventDevice> specification = createSpecification(criteria);
        return eventDeviceRepository.findAll(specification, page).map(eventDeviceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventDeviceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventDevice> specification = createSpecification(criteria);
        return eventDeviceRepository.count(specification);
    }

    /**
     * Function to convert {@link EventDeviceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventDevice> createSpecification(EventDeviceCriteria criteria) {
        Specification<EventDevice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), EventDevice_.id));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), EventDevice_.createdAt));
            }
            if (criteria.getTheoreticalPercentage() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getTheoreticalPercentage(), EventDevice_.theoreticalPercentage));
            }
            if (criteria.getMoneyDenomination() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMoneyDenomination(), EventDevice_.moneyDenomination));
            }
            if (criteria.getDeviceEstablishmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeviceEstablishmentId(),
                            root -> root.join(EventDevice_.deviceEstablishment, JoinType.LEFT).get(DeviceEstablishment_.id)
                        )
                    );
            }
            if (criteria.getEventTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEventTypeId(),
                            root -> root.join(EventDevice_.eventType, JoinType.LEFT).get(EventType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
