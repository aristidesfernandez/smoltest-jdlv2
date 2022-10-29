package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.CounterDevice;
import co.com.ies.smol.repository.CounterDeviceRepository;
import co.com.ies.smol.service.CounterDeviceService;
import co.com.ies.smol.service.dto.CounterDeviceDTO;
import co.com.ies.smol.service.mapper.CounterDeviceMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CounterDevice}.
 */
@Service
@Transactional
public class CounterDeviceServiceImpl implements CounterDeviceService {

    private final Logger log = LoggerFactory.getLogger(CounterDeviceServiceImpl.class);

    private final CounterDeviceRepository counterDeviceRepository;

    private final CounterDeviceMapper counterDeviceMapper;

    public CounterDeviceServiceImpl(CounterDeviceRepository counterDeviceRepository, CounterDeviceMapper counterDeviceMapper) {
        this.counterDeviceRepository = counterDeviceRepository;
        this.counterDeviceMapper = counterDeviceMapper;
    }

    @Override
    public CounterDeviceDTO save(CounterDeviceDTO counterDeviceDTO) {
        log.debug("Request to save CounterDevice : {}", counterDeviceDTO);
        CounterDevice counterDevice = counterDeviceMapper.toEntity(counterDeviceDTO);
        counterDevice = counterDeviceRepository.save(counterDevice);
        return counterDeviceMapper.toDto(counterDevice);
    }

    @Override
    public CounterDeviceDTO update(CounterDeviceDTO counterDeviceDTO) {
        log.debug("Request to update CounterDevice : {}", counterDeviceDTO);
        CounterDevice counterDevice = counterDeviceMapper.toEntity(counterDeviceDTO);
        counterDevice = counterDeviceRepository.save(counterDevice);
        return counterDeviceMapper.toDto(counterDevice);
    }

    @Override
    public Optional<CounterDeviceDTO> partialUpdate(CounterDeviceDTO counterDeviceDTO) {
        log.debug("Request to partially update CounterDevice : {}", counterDeviceDTO);

        return counterDeviceRepository
            .findById(counterDeviceDTO.getId())
            .map(existingCounterDevice -> {
                counterDeviceMapper.partialUpdate(existingCounterDevice, counterDeviceDTO);

                return existingCounterDevice;
            })
            .map(counterDeviceRepository::save)
            .map(counterDeviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CounterDeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CounterDevices");
        return counterDeviceRepository.findAll(pageable).map(counterDeviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CounterDeviceDTO> findOne(UUID id) {
        log.debug("Request to get CounterDevice : {}", id);
        return counterDeviceRepository.findById(id).map(counterDeviceMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CounterDevice : {}", id);
        counterDeviceRepository.deleteById(id);
    }
}
