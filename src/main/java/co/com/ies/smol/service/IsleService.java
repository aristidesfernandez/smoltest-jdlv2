package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.IsleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.Isle}.
 */
public interface IsleService {
    /**
     * Save a isle.
     *
     * @param isleDTO the entity to save.
     * @return the persisted entity.
     */
    IsleDTO save(IsleDTO isleDTO);

    /**
     * Updates a isle.
     *
     * @param isleDTO the entity to update.
     * @return the persisted entity.
     */
    IsleDTO update(IsleDTO isleDTO);

    /**
     * Partially updates a isle.
     *
     * @param isleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IsleDTO> partialUpdate(IsleDTO isleDTO);

    /**
     * Get all the isles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IsleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" isle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IsleDTO> findOne(Long id);

    /**
     * Delete the "id" isle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
