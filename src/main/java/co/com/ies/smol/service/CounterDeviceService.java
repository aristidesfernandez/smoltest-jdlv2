package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.CounterDeviceDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.CounterDevice}.
 */
public interface CounterDeviceService {
    /**
     * Save a counterDevice.
     *
     * @param counterDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    CounterDeviceDTO save(CounterDeviceDTO counterDeviceDTO);

    /**
     * Updates a counterDevice.
     *
     * @param counterDeviceDTO the entity to update.
     * @return the persisted entity.
     */
    CounterDeviceDTO update(CounterDeviceDTO counterDeviceDTO);

    /**
     * Partially updates a counterDevice.
     *
     * @param counterDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CounterDeviceDTO> partialUpdate(CounterDeviceDTO counterDeviceDTO);

    /**
     * Get all the counterDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CounterDeviceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" counterDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CounterDeviceDTO> findOne(UUID id);

    /**
     * Delete the "id" counterDevice.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
