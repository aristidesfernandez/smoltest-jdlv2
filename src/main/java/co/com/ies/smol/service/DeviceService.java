package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.DeviceDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.Device}.
 */
public interface DeviceService {
    /**
     * Save a device.
     *
     * @param deviceDTO the entity to save.
     * @return the persisted entity.
     */
    DeviceDTO save(DeviceDTO deviceDTO);

    /**
     * Updates a device.
     *
     * @param deviceDTO the entity to update.
     * @return the persisted entity.
     */
    DeviceDTO update(DeviceDTO deviceDTO);

    /**
     * Partially updates a device.
     *
     * @param deviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeviceDTO> partialUpdate(DeviceDTO deviceDTO);

    /**
     * Get all the devices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" device.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeviceDTO> findOne(UUID id);

    /**
     * Delete the "id" device.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
