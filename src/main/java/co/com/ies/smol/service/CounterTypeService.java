package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.CounterTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.CounterType}.
 */
public interface CounterTypeService {
    /**
     * Save a counterType.
     *
     * @param counterTypeDTO the entity to save.
     * @return the persisted entity.
     */
    CounterTypeDTO save(CounterTypeDTO counterTypeDTO);

    /**
     * Updates a counterType.
     *
     * @param counterTypeDTO the entity to update.
     * @return the persisted entity.
     */
    CounterTypeDTO update(CounterTypeDTO counterTypeDTO);

    /**
     * Partially updates a counterType.
     *
     * @param counterTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CounterTypeDTO> partialUpdate(CounterTypeDTO counterTypeDTO);

    /**
     * Get all the counterTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CounterTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" counterType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CounterTypeDTO> findOne(String id);

    /**
     * Delete the "id" counterType.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
