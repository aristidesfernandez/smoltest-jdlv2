package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.OperationalPropertiesEstablishment;
import co.com.ies.smol.repository.OperationalPropertiesEstablishmentRepository;
import co.com.ies.smol.service.criteria.OperationalPropertiesEstablishmentCriteria;
import co.com.ies.smol.service.dto.OperationalPropertiesEstablishmentDTO;
import co.com.ies.smol.service.mapper.OperationalPropertiesEstablishmentMapper;
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
 * Service for executing complex queries for {@link OperationalPropertiesEstablishment} entities in the database.
 * The main input is a {@link OperationalPropertiesEstablishmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OperationalPropertiesEstablishmentDTO} or a {@link Page} of {@link OperationalPropertiesEstablishmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OperationalPropertiesEstablishmentQueryService extends QueryService<OperationalPropertiesEstablishment> {

    private final Logger log = LoggerFactory.getLogger(OperationalPropertiesEstablishmentQueryService.class);

    private final OperationalPropertiesEstablishmentRepository operationalPropertiesEstablishmentRepository;

    private final OperationalPropertiesEstablishmentMapper operationalPropertiesEstablishmentMapper;

    public OperationalPropertiesEstablishmentQueryService(
        OperationalPropertiesEstablishmentRepository operationalPropertiesEstablishmentRepository,
        OperationalPropertiesEstablishmentMapper operationalPropertiesEstablishmentMapper
    ) {
        this.operationalPropertiesEstablishmentRepository = operationalPropertiesEstablishmentRepository;
        this.operationalPropertiesEstablishmentMapper = operationalPropertiesEstablishmentMapper;
    }

    /**
     * Return a {@link List} of {@link OperationalPropertiesEstablishmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OperationalPropertiesEstablishmentDTO> findByCriteria(OperationalPropertiesEstablishmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OperationalPropertiesEstablishment> specification = createSpecification(criteria);
        return operationalPropertiesEstablishmentMapper.toDto(operationalPropertiesEstablishmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OperationalPropertiesEstablishmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OperationalPropertiesEstablishmentDTO> findByCriteria(OperationalPropertiesEstablishmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OperationalPropertiesEstablishment> specification = createSpecification(criteria);
        return operationalPropertiesEstablishmentRepository
            .findAll(specification, page)
            .map(operationalPropertiesEstablishmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OperationalPropertiesEstablishmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OperationalPropertiesEstablishment> specification = createSpecification(criteria);
        return operationalPropertiesEstablishmentRepository.count(specification);
    }

    /**
     * Function to convert {@link OperationalPropertiesEstablishmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OperationalPropertiesEstablishment> createSpecification(OperationalPropertiesEstablishmentCriteria criteria) {
        Specification<OperationalPropertiesEstablishment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OperationalPropertiesEstablishment_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), OperationalPropertiesEstablishment_.value));
            }
            if (criteria.getKeyOperatingPropertyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getKeyOperatingPropertyId(),
                            root ->
                                root
                                    .join(OperationalPropertiesEstablishment_.keyOperatingProperty, JoinType.LEFT)
                                    .get(KeyOperatingProperty_.id)
                        )
                    );
            }
            if (criteria.getEstablishmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEstablishmentId(),
                            root -> root.join(OperationalPropertiesEstablishment_.establishment, JoinType.LEFT).get(Establishment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
