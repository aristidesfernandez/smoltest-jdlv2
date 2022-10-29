package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.CurrencyTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.CurrencyType}.
 */
public interface CurrencyTypeService {
    /**
     * Save a currencyType.
     *
     * @param currencyTypeDTO the entity to save.
     * @return the persisted entity.
     */
    CurrencyTypeDTO save(CurrencyTypeDTO currencyTypeDTO);

    /**
     * Updates a currencyType.
     *
     * @param currencyTypeDTO the entity to update.
     * @return the persisted entity.
     */
    CurrencyTypeDTO update(CurrencyTypeDTO currencyTypeDTO);

    /**
     * Partially updates a currencyType.
     *
     * @param currencyTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CurrencyTypeDTO> partialUpdate(CurrencyTypeDTO currencyTypeDTO);

    /**
     * Get all the currencyTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CurrencyTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" currencyType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CurrencyTypeDTO> findOne(Long id);

    /**
     * Delete the "id" currencyType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
