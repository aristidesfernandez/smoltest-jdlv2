package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.EstablishmentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.Establishment}.
 */
public interface EstablishmentService {
    /**
     * Save a establishment.
     *
     * @param establishmentDTO the entity to save.
     * @return the persisted entity.
     */
    EstablishmentDTO save(EstablishmentDTO establishmentDTO);

    /**
     * Updates a establishment.
     *
     * @param establishmentDTO the entity to update.
     * @return the persisted entity.
     */
    EstablishmentDTO update(EstablishmentDTO establishmentDTO);

    /**
     * Partially updates a establishment.
     *
     * @param establishmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EstablishmentDTO> partialUpdate(EstablishmentDTO establishmentDTO);

    /**
     * Get all the establishments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EstablishmentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" establishment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EstablishmentDTO> findOne(Long id);

    /**
     * Delete the "id" establishment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
