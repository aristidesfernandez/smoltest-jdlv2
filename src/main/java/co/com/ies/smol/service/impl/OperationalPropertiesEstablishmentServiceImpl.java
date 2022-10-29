package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.OperationalPropertiesEstablishment;
import co.com.ies.smol.repository.OperationalPropertiesEstablishmentRepository;
import co.com.ies.smol.service.OperationalPropertiesEstablishmentService;
import co.com.ies.smol.service.dto.OperationalPropertiesEstablishmentDTO;
import co.com.ies.smol.service.mapper.OperationalPropertiesEstablishmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OperationalPropertiesEstablishment}.
 */
@Service
@Transactional
public class OperationalPropertiesEstablishmentServiceImpl implements OperationalPropertiesEstablishmentService {

    private final Logger log = LoggerFactory.getLogger(OperationalPropertiesEstablishmentServiceImpl.class);

    private final OperationalPropertiesEstablishmentRepository operationalPropertiesEstablishmentRepository;

    private final OperationalPropertiesEstablishmentMapper operationalPropertiesEstablishmentMapper;

    public OperationalPropertiesEstablishmentServiceImpl(
        OperationalPropertiesEstablishmentRepository operationalPropertiesEstablishmentRepository,
        OperationalPropertiesEstablishmentMapper operationalPropertiesEstablishmentMapper
    ) {
        this.operationalPropertiesEstablishmentRepository = operationalPropertiesEstablishmentRepository;
        this.operationalPropertiesEstablishmentMapper = operationalPropertiesEstablishmentMapper;
    }

    @Override
    public OperationalPropertiesEstablishmentDTO save(OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO) {
        log.debug("Request to save OperationalPropertiesEstablishment : {}", operationalPropertiesEstablishmentDTO);
        OperationalPropertiesEstablishment operationalPropertiesEstablishment = operationalPropertiesEstablishmentMapper.toEntity(
            operationalPropertiesEstablishmentDTO
        );
        operationalPropertiesEstablishment = operationalPropertiesEstablishmentRepository.save(operationalPropertiesEstablishment);
        return operationalPropertiesEstablishmentMapper.toDto(operationalPropertiesEstablishment);
    }

    @Override
    public OperationalPropertiesEstablishmentDTO update(OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO) {
        log.debug("Request to update OperationalPropertiesEstablishment : {}", operationalPropertiesEstablishmentDTO);
        OperationalPropertiesEstablishment operationalPropertiesEstablishment = operationalPropertiesEstablishmentMapper.toEntity(
            operationalPropertiesEstablishmentDTO
        );
        operationalPropertiesEstablishment = operationalPropertiesEstablishmentRepository.save(operationalPropertiesEstablishment);
        return operationalPropertiesEstablishmentMapper.toDto(operationalPropertiesEstablishment);
    }

    @Override
    public Optional<OperationalPropertiesEstablishmentDTO> partialUpdate(
        OperationalPropertiesEstablishmentDTO operationalPropertiesEstablishmentDTO
    ) {
        log.debug("Request to partially update OperationalPropertiesEstablishment : {}", operationalPropertiesEstablishmentDTO);

        return operationalPropertiesEstablishmentRepository
            .findById(operationalPropertiesEstablishmentDTO.getId())
            .map(existingOperationalPropertiesEstablishment -> {
                operationalPropertiesEstablishmentMapper.partialUpdate(
                    existingOperationalPropertiesEstablishment,
                    operationalPropertiesEstablishmentDTO
                );

                return existingOperationalPropertiesEstablishment;
            })
            .map(operationalPropertiesEstablishmentRepository::save)
            .map(operationalPropertiesEstablishmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperationalPropertiesEstablishmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OperationalPropertiesEstablishments");
        return operationalPropertiesEstablishmentRepository.findAll(pageable).map(operationalPropertiesEstablishmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperationalPropertiesEstablishmentDTO> findOne(Long id) {
        log.debug("Request to get OperationalPropertiesEstablishment : {}", id);
        return operationalPropertiesEstablishmentRepository.findById(id).map(operationalPropertiesEstablishmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OperationalPropertiesEstablishment : {}", id);
        operationalPropertiesEstablishmentRepository.deleteById(id);
    }
}
