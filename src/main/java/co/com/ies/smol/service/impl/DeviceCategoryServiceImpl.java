package co.com.ies.smol.service.impl;

import co.com.ies.smol.domain.DeviceCategory;
import co.com.ies.smol.repository.DeviceCategoryRepository;
import co.com.ies.smol.service.DeviceCategoryService;
import co.com.ies.smol.service.dto.DeviceCategoryDTO;
import co.com.ies.smol.service.mapper.DeviceCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeviceCategory}.
 */
@Service
@Transactional
public class DeviceCategoryServiceImpl implements DeviceCategoryService {

    private final Logger log = LoggerFactory.getLogger(DeviceCategoryServiceImpl.class);

    private final DeviceCategoryRepository deviceCategoryRepository;

    private final DeviceCategoryMapper deviceCategoryMapper;

    public DeviceCategoryServiceImpl(DeviceCategoryRepository deviceCategoryRepository, DeviceCategoryMapper deviceCategoryMapper) {
        this.deviceCategoryRepository = deviceCategoryRepository;
        this.deviceCategoryMapper = deviceCategoryMapper;
    }

    @Override
    public DeviceCategoryDTO save(DeviceCategoryDTO deviceCategoryDTO) {
        log.debug("Request to save DeviceCategory : {}", deviceCategoryDTO);
        DeviceCategory deviceCategory = deviceCategoryMapper.toEntity(deviceCategoryDTO);
        deviceCategory = deviceCategoryRepository.save(deviceCategory);
        return deviceCategoryMapper.toDto(deviceCategory);
    }

    @Override
    public DeviceCategoryDTO update(DeviceCategoryDTO deviceCategoryDTO) {
        log.debug("Request to update DeviceCategory : {}", deviceCategoryDTO);
        DeviceCategory deviceCategory = deviceCategoryMapper.toEntity(deviceCategoryDTO);
        deviceCategory = deviceCategoryRepository.save(deviceCategory);
        return deviceCategoryMapper.toDto(deviceCategory);
    }

    @Override
    public Optional<DeviceCategoryDTO> partialUpdate(DeviceCategoryDTO deviceCategoryDTO) {
        log.debug("Request to partially update DeviceCategory : {}", deviceCategoryDTO);

        return deviceCategoryRepository
            .findById(deviceCategoryDTO.getId())
            .map(existingDeviceCategory -> {
                deviceCategoryMapper.partialUpdate(existingDeviceCategory, deviceCategoryDTO);

                return existingDeviceCategory;
            })
            .map(deviceCategoryRepository::save)
            .map(deviceCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeviceCategories");
        return deviceCategoryRepository.findAll(pageable).map(deviceCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceCategoryDTO> findOne(Long id) {
        log.debug("Request to get DeviceCategory : {}", id);
        return deviceCategoryRepository.findById(id).map(deviceCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeviceCategory : {}", id);
        deviceCategoryRepository.deleteById(id);
    }
}
