package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.Establishment;
import co.com.ies.smol.repository.EstablishmentRepository;
import co.com.ies.smol.service.EstablishmentService;
import co.com.ies.smol.service.dto.EstablishmentDTO;
import co.com.ies.smol.service.mapper.EstablishmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Establishment}.
 */
@Service
@Transactional
public class EstablishmentServiceImpl implements EstablishmentService {

    private final Logger log = LoggerFactory.getLogger(EstablishmentServiceImpl.class);

    private final EstablishmentRepository establishmentRepository;

    private final EstablishmentMapper establishmentMapper;

    public EstablishmentServiceImpl(EstablishmentRepository establishmentRepository, EstablishmentMapper establishmentMapper) {
        this.establishmentRepository = establishmentRepository;
        this.establishmentMapper = establishmentMapper;
    }

    @Override
    public EstablishmentDTO save(EstablishmentDTO establishmentDTO) {
        log.debug("Request to save Establishment : {}", establishmentDTO);
        Establishment establishment = establishmentMapper.toEntity(establishmentDTO);
        establishment = establishmentRepository.save(establishment);
        return establishmentMapper.toDto(establishment);
    }

    @Override
    public EstablishmentDTO update(EstablishmentDTO establishmentDTO) {
        log.debug("Request to update Establishment : {}", establishmentDTO);
        Establishment establishment = establishmentMapper.toEntity(establishmentDTO);
        establishment = establishmentRepository.save(establishment);
        return establishmentMapper.toDto(establishment);
    }

    @Override
    public Optional<EstablishmentDTO> partialUpdate(EstablishmentDTO establishmentDTO) {
        log.debug("Request to partially update Establishment : {}", establishmentDTO);

        return establishmentRepository
            .findById(establishmentDTO.getId())
            .map(existingEstablishment -> {
                establishmentMapper.partialUpdate(existingEstablishment, establishmentDTO);

                return existingEstablishment;
            })
            .map(establishmentRepository::save)
            .map(establishmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstablishmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Establishments");
        return establishmentRepository.findAll(pageable).map(establishmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstablishmentDTO> findOne(Long id) {
        log.debug("Request to get Establishment : {}", id);
        return establishmentRepository.findById(id).map(establishmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Establishment : {}", id);
        establishmentRepository.deleteById(id);
    }
}
