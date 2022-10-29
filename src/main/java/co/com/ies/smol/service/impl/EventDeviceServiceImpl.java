package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.EventDevice;
import co.com.ies.smol.repository.EventDeviceRepository;
import co.com.ies.smol.service.EventDeviceService;
import co.com.ies.smol.service.dto.EventDeviceDTO;
import co.com.ies.smol.service.mapper.EventDeviceMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventDevice}.
 */
@Service
@Transactional
public class EventDeviceServiceImpl implements EventDeviceService {

    private final Logger log = LoggerFactory.getLogger(EventDeviceServiceImpl.class);

    private final EventDeviceRepository eventDeviceRepository;

    private final EventDeviceMapper eventDeviceMapper;

    public EventDeviceServiceImpl(EventDeviceRepository eventDeviceRepository, EventDeviceMapper eventDeviceMapper) {
        this.eventDeviceRepository = eventDeviceRepository;
        this.eventDeviceMapper = eventDeviceMapper;
    }

    @Override
    public EventDeviceDTO save(EventDeviceDTO eventDeviceDTO) {
        log.debug("Request to save EventDevice : {}", eventDeviceDTO);
        EventDevice eventDevice = eventDeviceMapper.toEntity(eventDeviceDTO);
        eventDevice = eventDeviceRepository.save(eventDevice);
        return eventDeviceMapper.toDto(eventDevice);
    }

    @Override
    public EventDeviceDTO update(EventDeviceDTO eventDeviceDTO) {
        log.debug("Request to update EventDevice : {}", eventDeviceDTO);
        EventDevice eventDevice = eventDeviceMapper.toEntity(eventDeviceDTO);
        eventDevice = eventDeviceRepository.save(eventDevice);
        return eventDeviceMapper.toDto(eventDevice);
    }

    @Override
    public Optional<EventDeviceDTO> partialUpdate(EventDeviceDTO eventDeviceDTO) {
        log.debug("Request to partially update EventDevice : {}", eventDeviceDTO);

        return eventDeviceRepository
            .findById(eventDeviceDTO.getId())
            .map(existingEventDevice -> {
                eventDeviceMapper.partialUpdate(existingEventDevice, eventDeviceDTO);

                return existingEventDevice;
            })
            .map(eventDeviceRepository::save)
            .map(eventDeviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventDeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventDevices");
        return eventDeviceRepository.findAll(pageable).map(eventDeviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventDeviceDTO> findOne(UUID id) {
        log.debug("Request to get EventDevice : {}", id);
        return eventDeviceRepository.findById(id).map(eventDeviceMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete EventDevice : {}", id);
        eventDeviceRepository.deleteById(id);
    }
}
