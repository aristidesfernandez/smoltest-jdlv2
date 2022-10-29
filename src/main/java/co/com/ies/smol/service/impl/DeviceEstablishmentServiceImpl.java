package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.DeviceEstablishment;
import co.com.ies.smol.repository.DeviceEstablishmentRepository;
import co.com.ies.smol.service.DeviceEstablishmentService;
import co.com.ies.smol.service.dto.DeviceEstablishmentDTO;
import co.com.ies.smol.service.mapper.DeviceEstablishmentMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeviceEstablishment}.
 */
@Service
@Transactional
public class DeviceEstablishmentServiceImpl implements DeviceEstablishmentService {

    private final Logger log = LoggerFactory.getLogger(DeviceEstablishmentServiceImpl.class);

    private final DeviceEstablishmentRepository deviceEstablishmentRepository;

    private final DeviceEstablishmentMapper deviceEstablishmentMapper;

    public DeviceEstablishmentServiceImpl(
        DeviceEstablishmentRepository deviceEstablishmentRepository,
        DeviceEstablishmentMapper deviceEstablishmentMapper
    ) {
        this.deviceEstablishmentRepository = deviceEstablishmentRepository;
        this.deviceEstablishmentMapper = deviceEstablishmentMapper;
    }

    @Override
    public DeviceEstablishmentDTO save(DeviceEstablishmentDTO deviceEstablishmentDTO) {
        log.debug("Request to save DeviceEstablishment : {}", deviceEstablishmentDTO);
        DeviceEstablishment deviceEstablishment = deviceEstablishmentMapper.toEntity(deviceEstablishmentDTO);
        deviceEstablishment = deviceEstablishmentRepository.save(deviceEstablishment);
        return deviceEstablishmentMapper.toDto(deviceEstablishment);
    }

    @Override
    public DeviceEstablishmentDTO update(DeviceEstablishmentDTO deviceEstablishmentDTO) {
        log.debug("Request to update DeviceEstablishment : {}", deviceEstablishmentDTO);
        DeviceEstablishment deviceEstablishment = deviceEstablishmentMapper.toEntity(deviceEstablishmentDTO);
        deviceEstablishment = deviceEstablishmentRepository.save(deviceEstablishment);
        return deviceEstablishmentMapper.toDto(deviceEstablishment);
    }

    @Override
    public Optional<DeviceEstablishmentDTO> partialUpdate(DeviceEstablishmentDTO deviceEstablishmentDTO) {
        log.debug("Request to partially update DeviceEstablishment : {}", deviceEstablishmentDTO);

        return deviceEstablishmentRepository
            .findById(deviceEstablishmentDTO.getId())
            .map(existingDeviceEstablishment -> {
                deviceEstablishmentMapper.partialUpdate(existingDeviceEstablishment, deviceEstablishmentDTO);

                return existingDeviceEstablishment;
            })
            .map(deviceEstablishmentRepository::save)
            .map(deviceEstablishmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceEstablishmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeviceEstablishments");
        return deviceEstablishmentRepository.findAll(pageable).map(deviceEstablishmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceEstablishmentDTO> findOne(UUID id) {
        log.debug("Request to get DeviceEstablishment : {}", id);
        return deviceEstablishmentRepository.findById(id).map(deviceEstablishmentMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete DeviceEstablishment : {}", id);
        deviceEstablishmentRepository.deleteById(id);
    }
}
