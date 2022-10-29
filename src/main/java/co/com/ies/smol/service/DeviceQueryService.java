package co.com.ies.smol.service;

import co.com.ies.smol.domain.*; // for static metamodels
import co.com.ies.smol.domain.Device;
import co.com.ies.smol.repository.DeviceRepository;
import co.com.ies.smol.service.criteria.DeviceCriteria;
import co.com.ies.smol.service.dto.DeviceDTO;
import co.com.ies.smol.service.mapper.DeviceMapper;
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
 * Service for executing complex queries for {@link Device} entities in the database.
 * The main input is a {@link DeviceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeviceDTO} or a {@link Page} of {@link DeviceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeviceQueryService extends QueryService<Device> {

    private final Logger log = LoggerFactory.getLogger(DeviceQueryService.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    public DeviceQueryService(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    /**
     * Return a {@link List} of {@link DeviceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeviceDTO> findByCriteria(DeviceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Device> specification = createSpecification(criteria);
        return deviceMapper.toDto(deviceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DeviceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeviceDTO> findByCriteria(DeviceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Device> specification = createSpecification(criteria);
        return deviceRepository.findAll(specification, page).map(deviceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeviceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Device> specification = createSpecification(criteria);
        return deviceRepository.count(specification);
    }

    /**
     * Function to convert {@link DeviceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Device> createSpecification(DeviceCriteria criteria) {
        Specification<Device> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Device_.id));
            }
            if (criteria.getSerial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerial(), Device_.serial));
            }
            if (criteria.getIsProtocolEsdcs() != null) {
                specification = specification.and(buildSpecification(criteria.getIsProtocolEsdcs(), Device_.isProtocolEsdcs));
            }
            if (criteria.getNumberPlayedReport() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberPlayedReport(), Device_.numberPlayedReport));
            }
            if (criteria.getSasDenomination() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSasDenomination(), Device_.sasDenomination));
            }
            if (criteria.getIsMultigame() != null) {
                specification = specification.and(buildSpecification(criteria.getIsMultigame(), Device_.isMultigame));
            }
            if (criteria.getIsMultiDenomination() != null) {
                specification = specification.and(buildSpecification(criteria.getIsMultiDenomination(), Device_.isMultiDenomination));
            }
            if (criteria.getIsRetanqueo() != null) {
                specification = specification.and(buildSpecification(criteria.getIsRetanqueo(), Device_.isRetanqueo));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Device_.state));
            }
            if (criteria.getTheoreticalHold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTheoreticalHold(), Device_.theoreticalHold));
            }
            if (criteria.getSasIdentifier() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSasIdentifier(), Device_.sasIdentifier));
            }
            if (criteria.getCreditLimit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditLimit(), Device_.creditLimit));
            }
            if (criteria.getHasHooper() != null) {
                specification = specification.and(buildSpecification(criteria.getHasHooper(), Device_.hasHooper));
            }
            if (criteria.getColjuegosCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColjuegosCode(), Device_.coljuegosCode));
            }
            if (criteria.getFabricationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFabricationDate(), Device_.fabricationDate));
            }
            if (criteria.getCurrentToken() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentToken(), Device_.currentToken));
            }
            if (criteria.getDenominationTito() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDenominationTito(), Device_.denominationTito));
            }
            if (criteria.getEndLostCommunication() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getEndLostCommunication(), Device_.endLostCommunication));
            }
            if (criteria.getStartLostCommunication() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getStartLostCommunication(), Device_.startLostCommunication));
            }
            if (criteria.getReportMultiplier() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportMultiplier(), Device_.reportMultiplier));
            }
            if (criteria.getNuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNuid(), Device_.nuid));
            }
            if (criteria.getPayManualPrize() != null) {
                specification = specification.and(buildSpecification(criteria.getPayManualPrize(), Device_.payManualPrize));
            }
            if (criteria.getManualHandpay() != null) {
                specification = specification.and(buildSpecification(criteria.getManualHandpay(), Device_.manualHandpay));
            }
            if (criteria.getManualJackpot() != null) {
                specification = specification.and(buildSpecification(criteria.getManualJackpot(), Device_.manualJackpot));
            }
            if (criteria.getManualGameEvent() != null) {
                specification = specification.and(buildSpecification(criteria.getManualGameEvent(), Device_.manualGameEvent));
            }
            if (criteria.getBetCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBetCode(), Device_.betCode));
            }
            if (criteria.getHomologationIndicator() != null) {
                specification = specification.and(buildSpecification(criteria.getHomologationIndicator(), Device_.homologationIndicator));
            }
            if (criteria.getColjuegosModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColjuegosModel(), Device_.coljuegosModel));
            }
            if (criteria.getReportable() != null) {
                specification = specification.and(buildSpecification(criteria.getReportable(), Device_.reportable));
            }
            if (criteria.getAftDenomination() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAftDenomination(), Device_.aftDenomination));
            }
            if (criteria.getLastUpdateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdateDate(), Device_.lastUpdateDate));
            }
            if (criteria.getEnableRollover() != null) {
                specification = specification.and(buildSpecification(criteria.getEnableRollover(), Device_.enableRollover));
            }
            if (criteria.getLastCorruptionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastCorruptionDate(), Device_.lastCorruptionDate));
            }
            if (criteria.getDaftDenomination() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDaftDenomination(), Device_.daftDenomination));
            }
            if (criteria.getPrizesEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getPrizesEnabled(), Device_.prizesEnabled));
            }
            if (criteria.getCurrencyTypeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrencyTypeId(), Device_.currencyTypeId));
            }
            if (criteria.getIsleId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsleId(), Device_.isleId));
            }
            if (criteria.getModelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getModelId(), root -> root.join(Device_.model, JoinType.LEFT).get(Model_.id))
                    );
            }
            if (criteria.getDeviceCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeviceCategoryId(),
                            root -> root.join(Device_.deviceCategory, JoinType.LEFT).get(DeviceCategory_.id)
                        )
                    );
            }
            if (criteria.getDeviceTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeviceTypeId(),
                            root -> root.join(Device_.deviceType, JoinType.LEFT).get(DeviceType_.id)
                        )
                    );
            }
            if (criteria.getFormulaHandpayId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFormulaHandpayId(),
                            root -> root.join(Device_.formulaHandpay, JoinType.LEFT).get(Formula_.id)
                        )
                    );
            }
            if (criteria.getFormulaJackpotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFormulaJackpotId(),
                            root -> root.join(Device_.formulaJackpot, JoinType.LEFT).get(Formula_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
