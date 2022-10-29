package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.ManufacturerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.Manufacturer}.
 */
public interface ManufacturerService {
    /**
     * Save a manufacturer.
     *
     * @param manufacturerDTO the entity to save.
     * @return the persisted entity.
     */
    ManufacturerDTO save(ManufacturerDTO manufacturerDTO);

    /**
     * Updates a manufacturer.
     *
     * @param manufacturerDTO the entity to update.
     * @return the persisted entity.
     */
    ManufacturerDTO update(ManufacturerDTO manufacturerDTO);

    /**
     * Partially updates a manufacturer.
     *
     * @param manufacturerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ManufacturerDTO> partialUpdate(ManufacturerDTO manufacturerDTO);

    /**
     * Get all the manufacturers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ManufacturerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" manufacturer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManufacturerDTO> findOne(Long id);

    /**
     * Delete the "id" manufacturer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
