package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.EventDeviceDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.EventDevice}.
 */
public interface EventDeviceService {
    /**
     * Save a eventDevice.
     *
     * @param eventDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    EventDeviceDTO save(EventDeviceDTO eventDeviceDTO);

    /**
     * Updates a eventDevice.
     *
     * @param eventDeviceDTO the entity to update.
     * @return the persisted entity.
     */
    EventDeviceDTO update(EventDeviceDTO eventDeviceDTO);

    /**
     * Partially updates a eventDevice.
     *
     * @param eventDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventDeviceDTO> partialUpdate(EventDeviceDTO eventDeviceDTO);

    /**
     * Get all the eventDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventDeviceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventDeviceDTO> findOne(UUID id);

    /**
     * Delete the "id" eventDevice.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
