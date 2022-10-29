package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.CounterEventDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.CounterEvent}.
 */
public interface CounterEventService {
    /**
     * Save a counterEvent.
     *
     * @param counterEventDTO the entity to save.
     * @return the persisted entity.
     */
    CounterEventDTO save(CounterEventDTO counterEventDTO);

    /**
     * Updates a counterEvent.
     *
     * @param counterEventDTO the entity to update.
     * @return the persisted entity.
     */
    CounterEventDTO update(CounterEventDTO counterEventDTO);

    /**
     * Partially updates a counterEvent.
     *
     * @param counterEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CounterEventDTO> partialUpdate(CounterEventDTO counterEventDTO);

    /**
     * Get all the counterEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CounterEventDTO> findAll(Pageable pageable);

    /**
     * Get the "id" counterEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CounterEventDTO> findOne(UUID id);

    /**
     * Delete the "id" counterEvent.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
