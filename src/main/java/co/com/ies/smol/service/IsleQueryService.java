package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.Isle;
import co.com.ies.smol.repository.IsleRepository;
import co.com.ies.smol.service.criteria.IsleCriteria;
import co.com.ies.smol.service.dto.IsleDTO;
import co.com.ies.smol.service.mapper.IsleMapper;
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
 * Service for executing complex queries for {@link Isle} entities in the database.
 * The main input is a {@link IsleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IsleDTO} or a {@link Page} of {@link IsleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IsleQueryService extends QueryService<Isle> {

    private final Logger log = LoggerFactory.getLogger(IsleQueryService.class);

    private final IsleRepository isleRepository;

    private final IsleMapper isleMapper;

    public IsleQueryService(IsleRepository isleRepository, IsleMapper isleMapper) {
        this.isleRepository = isleRepository;
        this.isleMapper = isleMapper;
    }

    /**
     * Return a {@link List} of {@link IsleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IsleDTO> findByCriteria(IsleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Isle> specification = createSpecification(criteria);
        return isleMapper.toDto(isleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IsleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IsleDTO> findByCriteria(IsleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Isle> specification = createSpecification(criteria);
        return isleRepository.findAll(specification, page).map(isleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IsleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Isle> specification = createSpecification(criteria);
        return isleRepository.count(specification);
    }

    /**
     * Function to convert {@link IsleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Isle> createSpecification(IsleCriteria criteria) {
        Specification<Isle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Isle_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Isle_.description));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Isle_.name));
            }
        }
        return specification;
    }
}
