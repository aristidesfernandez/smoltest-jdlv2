package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.KeyOperatingProperty;
import co.com.ies.smol.repository.KeyOperatingPropertyRepository;
import co.com.ies.smol.service.KeyOperatingPropertyService;
import co.com.ies.smol.service.dto.KeyOperatingPropertyDTO;
import co.com.ies.smol.service.mapper.KeyOperatingPropertyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link KeyOperatingProperty}.
 */
@Service
@Transactional
public class KeyOperatingPropertyServiceImpl implements KeyOperatingPropertyService {

    private final Logger log = LoggerFactory.getLogger(KeyOperatingPropertyServiceImpl.class);

    private final KeyOperatingPropertyRepository keyOperatingPropertyRepository;

    private final KeyOperatingPropertyMapper keyOperatingPropertyMapper;

    public KeyOperatingPropertyServiceImpl(
        KeyOperatingPropertyRepository keyOperatingPropertyRepository,
        KeyOperatingPropertyMapper keyOperatingPropertyMapper
    ) {
        this.keyOperatingPropertyRepository = keyOperatingPropertyRepository;
        this.keyOperatingPropertyMapper = keyOperatingPropertyMapper;
    }

    @Override
    public KeyOperatingPropertyDTO save(KeyOperatingPropertyDTO keyOperatingPropertyDTO) {
        log.debug("Request to save KeyOperatingProperty : {}", keyOperatingPropertyDTO);
        KeyOperatingProperty keyOperatingProperty = keyOperatingPropertyMapper.toEntity(keyOperatingPropertyDTO);
        keyOperatingProperty = keyOperatingPropertyRepository.save(keyOperatingProperty);
        return keyOperatingPropertyMapper.toDto(keyOperatingProperty);
    }

    @Override
    public KeyOperatingPropertyDTO update(KeyOperatingPropertyDTO keyOperatingPropertyDTO) {
        log.debug("Request to update KeyOperatingProperty : {}", keyOperatingPropertyDTO);
        KeyOperatingProperty keyOperatingProperty = keyOperatingPropertyMapper.toEntity(keyOperatingPropertyDTO);
        keyOperatingProperty = keyOperatingPropertyRepository.save(keyOperatingProperty);
        return keyOperatingPropertyMapper.toDto(keyOperatingProperty);
    }

    @Override
    public Optional<KeyOperatingPropertyDTO> partialUpdate(KeyOperatingPropertyDTO keyOperatingPropertyDTO) {
        log.debug("Request to partially update KeyOperatingProperty : {}", keyOperatingPropertyDTO);

        return keyOperatingPropertyRepository
            .findById(keyOperatingPropertyDTO.getId())
            .map(existingKeyOperatingProperty -> {
                keyOperatingPropertyMapper.partialUpdate(existingKeyOperatingProperty, keyOperatingPropertyDTO);

                return existingKeyOperatingProperty;
            })
            .map(keyOperatingPropertyRepository::save)
            .map(keyOperatingPropertyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KeyOperatingPropertyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all KeyOperatingProperties");
        return keyOperatingPropertyRepository.findAll(pageable).map(keyOperatingPropertyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KeyOperatingPropertyDTO> findOne(Long id) {
        log.debug("Request to get KeyOperatingProperty : {}", id);
        return keyOperatingPropertyRepository.findById(id).map(keyOperatingPropertyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete KeyOperatingProperty : {}", id);
        keyOperatingPropertyRepository.deleteById(id);
    }
}
