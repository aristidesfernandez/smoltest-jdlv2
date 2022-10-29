package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.DeviceInterfaceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.DeviceInterface}.
 */
public interface DeviceInterfaceService {
    /**
     * Save a deviceInterface.
     *
     * @param deviceInterfaceDTO the entity to save.
     * @return the persisted entity.
     */
    DeviceInterfaceDTO save(DeviceInterfaceDTO deviceInterfaceDTO);

    /**
     * Updates a deviceInterface.
     *
     * @param deviceInterfaceDTO the entity to update.
     * @return the persisted entity.
     */
    DeviceInterfaceDTO update(DeviceInterfaceDTO deviceInterfaceDTO);

    /**
     * Partially updates a deviceInterface.
     *
     * @param deviceInterfaceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeviceInterfaceDTO> partialUpdate(DeviceInterfaceDTO deviceInterfaceDTO);

    /**
     * Get all the deviceInterfaces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceInterfaceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" deviceInterface.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeviceInterfaceDTO> findOne(Long id);

    /**
     * Delete the "id" deviceInterface.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
