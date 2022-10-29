package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.KeyOperatingPropertyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.KeyOperatingProperty}.
 */
public interface KeyOperatingPropertyService {
    /**
     * Save a keyOperatingProperty.
     *
     * @param keyOperatingPropertyDTO the entity to save.
     * @return the persisted entity.
     */
    KeyOperatingPropertyDTO save(KeyOperatingPropertyDTO keyOperatingPropertyDTO);

    /**
     * Updates a keyOperatingProperty.
     *
     * @param keyOperatingPropertyDTO the entity to update.
     * @return the persisted entity.
     */
    KeyOperatingPropertyDTO update(KeyOperatingPropertyDTO keyOperatingPropertyDTO);

    /**
     * Partially updates a keyOperatingProperty.
     *
     * @param keyOperatingPropertyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<KeyOperatingPropertyDTO> partialUpdate(KeyOperatingPropertyDTO keyOperatingPropertyDTO);

    /**
     * Get all the keyOperatingProperties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<KeyOperatingPropertyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" keyOperatingProperty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KeyOperatingPropertyDTO> findOne(Long id);

    /**
     * Delete the "id" keyOperatingProperty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
