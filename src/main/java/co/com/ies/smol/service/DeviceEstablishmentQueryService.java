package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.DeviceEstablishment;
import co.com.ies.smol.repository.DeviceEstablishmentRepository;
import co.com.ies.smol.service.criteria.DeviceEstablishmentCriteria;
import co.com.ies.smol.service.dto.DeviceEstablishmentDTO;
import co.com.ies.smol.service.mapper.DeviceEstablishmentMapper;
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
 * Service for executing complex queries for {@link DeviceEstablishment} entities in the database.
 * The main input is a {@link DeviceEstablishmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeviceEstablishmentDTO} or a {@link Page} of {@link DeviceEstablishmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeviceEstablishmentQueryService extends QueryService<DeviceEstablishment> {

    private final Logger log = LoggerFactory.getLogger(DeviceEstablishmentQueryService.class);

    private final DeviceEstablishmentRepository deviceEstablishmentRepository;

    private final DeviceEstablishmentMapper deviceEstablishmentMapper;

    public DeviceEstablishmentQueryService(
        DeviceEstablishmentRepository deviceEstablishmentRepository,
        DeviceEstablishmentMapper deviceEstablishmentMapper
    ) {
        this.deviceEstablishmentRepository = deviceEstablishmentRepository;
        this.deviceEstablishmentMapper = deviceEstablishmentMapper;
    }

    /**
     * Return a {@link List} of {@link DeviceEstablishmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeviceEstablishmentDTO> findByCriteria(DeviceEstablishmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DeviceEstablishment> specification = createSpecification(criteria);
        return deviceEstablishmentMapper.toDto(deviceEstablishmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DeviceEstablishmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeviceEstablishmentDTO> findByCriteria(DeviceEstablishmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DeviceEstablishment> specification = createSpecification(criteria);
        return deviceEstablishmentRepository.findAll(specification, page).map(deviceEstablishmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeviceEstablishmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DeviceEstablishment> specification = createSpecification(criteria);
        return deviceEstablishmentRepository.count(specification);
    }

    /**
     * Function to convert {@link DeviceEstablishmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DeviceEstablishment> createSpecification(DeviceEstablishmentCriteria criteria) {
        Specification<DeviceEstablishment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DeviceEstablishment_.id));
            }
            if (criteria.getRegistrationAt() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRegistrationAt(), DeviceEstablishment_.registrationAt));
            }
            if (criteria.getDepartureAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDepartureAt(), DeviceEstablishment_.departureAt));
            }
            if (criteria.getDeviceNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeviceNumber(), DeviceEstablishment_.deviceNumber));
            }
            if (criteria.getConsecutiveDevice() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getConsecutiveDevice(), DeviceEstablishment_.consecutiveDevice));
            }
            if (criteria.getEstablishmentId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getEstablishmentId(), DeviceEstablishment_.establishmentId));
            }
            if (criteria.getNegativeAward() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNegativeAward(), DeviceEstablishment_.negativeAward));
            }
            if (criteria.getDeviceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeviceId(),
                            root -> root.join(DeviceEstablishment_.device, JoinType.LEFT).get(Device_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
