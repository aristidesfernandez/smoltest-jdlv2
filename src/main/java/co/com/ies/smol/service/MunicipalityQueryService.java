package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.Municipality;
import co.com.ies.smol.repository.MunicipalityRepository;
import co.com.ies.smol.service.criteria.MunicipalityCriteria;
import co.com.ies.smol.service.dto.MunicipalityDTO;
import co.com.ies.smol.service.mapper.MunicipalityMapper;
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
 * Service for executing complex queries for {@link Municipality} entities in the database.
 * The main input is a {@link MunicipalityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MunicipalityDTO} or a {@link Page} of {@link MunicipalityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MunicipalityQueryService extends QueryService<Municipality> {

    private final Logger log = LoggerFactory.getLogger(MunicipalityQueryService.class);

    private final MunicipalityRepository municipalityRepository;

    private final MunicipalityMapper municipalityMapper;

    public MunicipalityQueryService(MunicipalityRepository municipalityRepository, MunicipalityMapper municipalityMapper) {
        this.municipalityRepository = municipalityRepository;
        this.municipalityMapper = municipalityMapper;
    }

    /**
     * Return a {@link List} of {@link MunicipalityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MunicipalityDTO> findByCriteria(MunicipalityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Municipality> specification = createSpecification(criteria);
        return municipalityMapper.toDto(municipalityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MunicipalityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MunicipalityDTO> findByCriteria(MunicipalityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Municipality> specification = createSpecification(criteria);
        return municipalityRepository.findAll(specification, page).map(municipalityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MunicipalityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Municipality> specification = createSpecification(criteria);
        return municipalityRepository.count(specification);
    }

    /**
     * Function to convert {@link MunicipalityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Municipality> createSpecification(MunicipalityCriteria criteria) {
        Specification<Municipality> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Municipality_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Municipality_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Municipality_.name));
            }
            if (criteria.getDaneCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDaneCode(), Municipality_.daneCode));
            }
            if (criteria.getProvinceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProvinceId(),
                            root -> root.join(Municipality_.province, JoinType.LEFT).get(Province_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
