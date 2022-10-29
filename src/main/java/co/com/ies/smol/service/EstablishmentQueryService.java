package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.Establishment;
import co.com.ies.smol.repository.EstablishmentRepository;
import co.com.ies.smol.service.criteria.EstablishmentCriteria;
import co.com.ies.smol.service.dto.EstablishmentDTO;
import co.com.ies.smol.service.mapper.EstablishmentMapper;
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
 * Service for executing complex queries for {@link Establishment} entities in the database.
 * The main input is a {@link EstablishmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EstablishmentDTO} or a {@link Page} of {@link EstablishmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstablishmentQueryService extends QueryService<Establishment> {

    private final Logger log = LoggerFactory.getLogger(EstablishmentQueryService.class);

    private final EstablishmentRepository establishmentRepository;

    private final EstablishmentMapper establishmentMapper;

    public EstablishmentQueryService(EstablishmentRepository establishmentRepository, EstablishmentMapper establishmentMapper) {
        this.establishmentRepository = establishmentRepository;
        this.establishmentMapper = establishmentMapper;
    }

    /**
     * Return a {@link List} of {@link EstablishmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EstablishmentDTO> findByCriteria(EstablishmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Establishment> specification = createSpecification(criteria);
        return establishmentMapper.toDto(establishmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EstablishmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstablishmentDTO> findByCriteria(EstablishmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Establishment> specification = createSpecification(criteria);
        return establishmentRepository.findAll(specification, page).map(establishmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstablishmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Establishment> specification = createSpecification(criteria);
        return establishmentRepository.count(specification);
    }

    /**
     * Function to convert {@link EstablishmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Establishment> createSpecification(EstablishmentCriteria criteria) {
        Specification<Establishment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Establishment_.id));
            }
            if (criteria.getLiquidationTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLiquidationTime(), Establishment_.liquidationTime));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Establishment_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Establishment_.type));
            }
            if (criteria.getNeighborhood() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNeighborhood(), Establishment_.neighborhood));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Establishment_.address));
            }
            if (criteria.getColjuegosCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColjuegosCode(), Establishment_.coljuegosCode));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Establishment_.startTime));
            }
            if (criteria.getCloseTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCloseTime(), Establishment_.closeTime));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Establishment_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Establishment_.latitude));
            }
            if (criteria.getMercantileRegistration() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getMercantileRegistration(), Establishment_.mercantileRegistration)
                    );
            }
            if (criteria.getOperatorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOperatorId(),
                            root -> root.join(Establishment_.operator, JoinType.LEFT).get(Operator_.id)
                        )
                    );
            }
            if (criteria.getMunicipalityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMunicipalityId(),
                            root -> root.join(Establishment_.municipality, JoinType.LEFT).get(Municipality_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
