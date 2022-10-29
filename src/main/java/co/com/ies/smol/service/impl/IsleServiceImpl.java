package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.Isle;
import co.com.ies.smol.repository.IsleRepository;
import co.com.ies.smol.service.IsleService;
import co.com.ies.smol.service.dto.IsleDTO;
import co.com.ies.smol.service.mapper.IsleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Isle}.
 */
@Service
@Transactional
public class IsleServiceImpl implements IsleService {

    private final Logger log = LoggerFactory.getLogger(IsleServiceImpl.class);

    private final IsleRepository isleRepository;

    private final IsleMapper isleMapper;

    public IsleServiceImpl(IsleRepository isleRepository, IsleMapper isleMapper) {
        this.isleRepository = isleRepository;
        this.isleMapper = isleMapper;
    }

    @Override
    public IsleDTO save(IsleDTO isleDTO) {
        log.debug("Request to save Isle : {}", isleDTO);
        Isle isle = isleMapper.toEntity(isleDTO);
        isle = isleRepository.save(isle);
        return isleMapper.toDto(isle);
    }

    @Override
    public IsleDTO update(IsleDTO isleDTO) {
        log.debug("Request to update Isle : {}", isleDTO);
        Isle isle = isleMapper.toEntity(isleDTO);
        isle = isleRepository.save(isle);
        return isleMapper.toDto(isle);
    }

    @Override
    public Optional<IsleDTO> partialUpdate(IsleDTO isleDTO) {
        log.debug("Request to partially update Isle : {}", isleDTO);

        return isleRepository
            .findById(isleDTO.getId())
            .map(existingIsle -> {
                isleMapper.partialUpdate(existingIsle, isleDTO);

                return existingIsle;
            })
            .map(isleRepository::save)
            .map(isleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Isles");
        return isleRepository.findAll(pageable).map(isleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IsleDTO> findOne(Long id) {
        log.debug("Request to get Isle : {}", id);
        return isleRepository.findById(id).map(isleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Isle : {}", id);
        isleRepository.deleteById(id);
    }
}
