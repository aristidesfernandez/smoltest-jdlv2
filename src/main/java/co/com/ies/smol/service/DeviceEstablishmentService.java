package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.DeviceEstablishmentDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.DeviceEstablishment}.
 */
public interface DeviceEstablishmentService {
    /**
     * Save a deviceEstablishment.
     *
     * @param deviceEstablishmentDTO the entity to save.
     * @return the persisted entity.
     */
    DeviceEstablishmentDTO save(DeviceEstablishmentDTO deviceEstablishmentDTO);

    /**
     * Updates a deviceEstablishment.
     *
     * @param deviceEstablishmentDTO the entity to update.
     * @return the persisted entity.
     */
    DeviceEstablishmentDTO update(DeviceEstablishmentDTO deviceEstablishmentDTO);

    /**
     * Partially updates a deviceEstablishment.
     *
     * @param deviceEstablishmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeviceEstablishmentDTO> partialUpdate(DeviceEstablishmentDTO deviceEstablishmentDTO);

    /**
     * Get all the deviceEstablishments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceEstablishmentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" deviceEstablishment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeviceEstablishmentDTO> findOne(UUID id);

    /**
     * Delete the "id" deviceEstablishment.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
