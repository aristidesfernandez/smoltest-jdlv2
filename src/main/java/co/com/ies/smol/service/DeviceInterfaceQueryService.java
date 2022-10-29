package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.DeviceInterface;
import co.com.ies.smol.repository.DeviceInterfaceRepository;
import co.com.ies.smol.service.criteria.DeviceInterfaceCriteria;
import co.com.ies.smol.service.dto.DeviceInterfaceDTO;
import co.com.ies.smol.service.mapper.DeviceInterfaceMapper;
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
 * Service for executing complex queries for {@link DeviceInterface} entities in the database.
 * The main input is a {@link DeviceInterfaceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeviceInterfaceDTO} or a {@link Page} of {@link DeviceInterfaceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeviceInterfaceQueryService extends QueryService<DeviceInterface> {

    private final Logger log = LoggerFactory.getLogger(DeviceInterfaceQueryService.class);

    private final DeviceInterfaceRepository deviceInterfaceRepository;

    private final DeviceInterfaceMapper deviceInterfaceMapper;

    public DeviceInterfaceQueryService(DeviceInterfaceRepository deviceInterfaceRepository, DeviceInterfaceMapper deviceInterfaceMapper) {
        this.deviceInterfaceRepository = deviceInterfaceRepository;
        this.deviceInterfaceMapper = deviceInterfaceMapper;
    }

    /**
     * Return a {@link List} of {@link DeviceInterfaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeviceInterfaceDTO> findByCriteria(DeviceInterfaceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DeviceInterface> specification = createSpecification(criteria);
        return deviceInterfaceMapper.toDto(deviceInterfaceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DeviceInterfaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeviceInterfaceDTO> findByCriteria(DeviceInterfaceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DeviceInterface> specification = createSpecification(criteria);
        return deviceInterfaceRepository.findAll(specification, page).map(deviceInterfaceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeviceInterfaceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DeviceInterface> specification = createSpecification(criteria);
        return deviceInterfaceRepository.count(specification);
    }

    /**
     * Function to convert {@link DeviceInterfaceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DeviceInterface> createSpecification(DeviceInterfaceCriteria criteria) {
        Specification<DeviceInterface> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DeviceInterface_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), DeviceInterface_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), DeviceInterface_.endDate));
            }
            if (criteria.getEstablishmentId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstablishmentId(), DeviceInterface_.establishmentId));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), DeviceInterface_.state));
            }
            if (criteria.getDeviceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeviceId(),
                            root -> root.join(DeviceInterface_.device, JoinType.LEFT).get(Device_.id)
                        )
                    );
            }
            if (criteria.getInterfaceBoardId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInterfaceBoardId(),
                            root -> root.join(DeviceInterface_.interfaceBoard, JoinType.LEFT).get(InterfaceBoard_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
