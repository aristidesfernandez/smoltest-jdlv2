package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.DeviceInterface;
import co.com.ies.smol.repository.DeviceInterfaceRepository;
import co.com.ies.smol.service.DeviceInterfaceService;
import co.com.ies.smol.service.dto.DeviceInterfaceDTO;
import co.com.ies.smol.service.mapper.DeviceInterfaceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeviceInterface}.
 */
@Service
@Transactional
public class DeviceInterfaceServiceImpl implements DeviceInterfaceService {

    private final Logger log = LoggerFactory.getLogger(DeviceInterfaceServiceImpl.class);

    private final DeviceInterfaceRepository deviceInterfaceRepository;

    private final DeviceInterfaceMapper deviceInterfaceMapper;

    public DeviceInterfaceServiceImpl(DeviceInterfaceRepository deviceInterfaceRepository, DeviceInterfaceMapper deviceInterfaceMapper) {
        this.deviceInterfaceRepository = deviceInterfaceRepository;
        this.deviceInterfaceMapper = deviceInterfaceMapper;
    }

    @Override
    public DeviceInterfaceDTO save(DeviceInterfaceDTO deviceInterfaceDTO) {
        log.debug("Request to save DeviceInterface : {}", deviceInterfaceDTO);
        DeviceInterface deviceInterface = deviceInterfaceMapper.toEntity(deviceInterfaceDTO);
        deviceInterface = deviceInterfaceRepository.save(deviceInterface);
        return deviceInterfaceMapper.toDto(deviceInterface);
    }

    @Override
    public DeviceInterfaceDTO update(DeviceInterfaceDTO deviceInterfaceDTO) {
        log.debug("Request to update DeviceInterface : {}", deviceInterfaceDTO);
        DeviceInterface deviceInterface = deviceInterfaceMapper.toEntity(deviceInterfaceDTO);
        deviceInterface = deviceInterfaceRepository.save(deviceInterface);
        return deviceInterfaceMapper.toDto(deviceInterface);
    }

    @Override
    public Optional<DeviceInterfaceDTO> partialUpdate(DeviceInterfaceDTO deviceInterfaceDTO) {
        log.debug("Request to partially update DeviceInterface : {}", deviceInterfaceDTO);

        return deviceInterfaceRepository
            .findById(deviceInterfaceDTO.getId())
            .map(existingDeviceInterface -> {
                deviceInterfaceMapper.partialUpdate(existingDeviceInterface, deviceInterfaceDTO);

                return existingDeviceInterface;
            })
            .map(deviceInterfaceRepository::save)
            .map(deviceInterfaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceInterfaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeviceInterfaces");
        return deviceInterfaceRepository.findAll(pageable).map(deviceInterfaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceInterfaceDTO> findOne(Long id) {
        log.debug("Request to get DeviceInterface : {}", id);
        return deviceInterfaceRepository.findById(id).map(deviceInterfaceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeviceInterface : {}", id);
        deviceInterfaceRepository.deleteById(id);
    }
}
