package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.CounterType;
import co.com.ies.smol.repository.CounterTypeRepository;
import co.com.ies.smol.service.CounterTypeService;
import co.com.ies.smol.service.dto.CounterTypeDTO;
import co.com.ies.smol.service.mapper.CounterTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CounterType}.
 */
@Service
@Transactional
public class CounterTypeServiceImpl implements CounterTypeService {

    private final Logger log = LoggerFactory.getLogger(CounterTypeServiceImpl.class);

    private final CounterTypeRepository counterTypeRepository;

    private final CounterTypeMapper counterTypeMapper;

    public CounterTypeServiceImpl(CounterTypeRepository counterTypeRepository, CounterTypeMapper counterTypeMapper) {
        this.counterTypeRepository = counterTypeRepository;
        this.counterTypeMapper = counterTypeMapper;
    }

    @Override
    public CounterTypeDTO save(CounterTypeDTO counterTypeDTO) {
        log.debug("Request to save CounterType : {}", counterTypeDTO);
        CounterType counterType = counterTypeMapper.toEntity(counterTypeDTO);
        counterType = counterTypeRepository.save(counterType);
        return counterTypeMapper.toDto(counterType);
    }

    @Override
    public CounterTypeDTO update(CounterTypeDTO counterTypeDTO) {
        log.debug("Request to update CounterType : {}", counterTypeDTO);
        CounterType counterType = counterTypeMapper.toEntity(counterTypeDTO);
        counterType.setIsPersisted();
        counterType = counterTypeRepository.save(counterType);
        return counterTypeMapper.toDto(counterType);
    }

    @Override
    public Optional<CounterTypeDTO> partialUpdate(CounterTypeDTO counterTypeDTO) {
        log.debug("Request to partially update CounterType : {}", counterTypeDTO);

        return counterTypeRepository
            .findById(counterTypeDTO.getCounterCode())
            .map(existingCounterType -> {
                counterTypeMapper.partialUpdate(existingCounterType, counterTypeDTO);

                return existingCounterType;
            })
            .map(counterTypeRepository::save)
            .map(counterTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CounterTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CounterTypes");
        return counterTypeRepository.findAll(pageable).map(counterTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CounterTypeDTO> findOne(String id) {
        log.debug("Request to get CounterType : {}", id);
        return counterTypeRepository.findById(id).map(counterTypeMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete CounterType : {}", id);
        counterTypeRepository.deleteById(id);
    }
}
