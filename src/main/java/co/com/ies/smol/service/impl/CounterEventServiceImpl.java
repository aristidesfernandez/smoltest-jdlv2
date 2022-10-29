package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.CounterEvent;
import co.com.ies.smol.repository.CounterEventRepository;
import co.com.ies.smol.service.CounterEventService;
import co.com.ies.smol.service.dto.CounterEventDTO;
import co.com.ies.smol.service.mapper.CounterEventMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CounterEvent}.
 */
@Service
@Transactional
public class CounterEventServiceImpl implements CounterEventService {

    private final Logger log = LoggerFactory.getLogger(CounterEventServiceImpl.class);

    private final CounterEventRepository counterEventRepository;

    private final CounterEventMapper counterEventMapper;

    public CounterEventServiceImpl(CounterEventRepository counterEventRepository, CounterEventMapper counterEventMapper) {
        this.counterEventRepository = counterEventRepository;
        this.counterEventMapper = counterEventMapper;
    }

    @Override
    public CounterEventDTO save(CounterEventDTO counterEventDTO) {
        log.debug("Request to save CounterEvent : {}", counterEventDTO);
        CounterEvent counterEvent = counterEventMapper.toEntity(counterEventDTO);
        counterEvent = counterEventRepository.save(counterEvent);
        return counterEventMapper.toDto(counterEvent);
    }

    @Override
    public CounterEventDTO update(CounterEventDTO counterEventDTO) {
        log.debug("Request to update CounterEvent : {}", counterEventDTO);
        CounterEvent counterEvent = counterEventMapper.toEntity(counterEventDTO);
        counterEvent = counterEventRepository.save(counterEvent);
        return counterEventMapper.toDto(counterEvent);
    }

    @Override
    public Optional<CounterEventDTO> partialUpdate(CounterEventDTO counterEventDTO) {
        log.debug("Request to partially update CounterEvent : {}", counterEventDTO);

        return counterEventRepository
            .findById(counterEventDTO.getId())
            .map(existingCounterEvent -> {
                counterEventMapper.partialUpdate(existingCounterEvent, counterEventDTO);

                return existingCounterEvent;
            })
            .map(counterEventRepository::save)
            .map(counterEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CounterEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CounterEvents");
        return counterEventRepository.findAll(pageable).map(counterEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CounterEventDTO> findOne(UUID id) {
        log.debug("Request to get CounterEvent : {}", id);
        return counterEventRepository.findById(id).map(counterEventMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CounterEvent : {}", id);
        counterEventRepository.deleteById(id);
    }
}
