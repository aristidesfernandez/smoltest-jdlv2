package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.FormulaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.Formula}.
 */
public interface FormulaService {
    /**
     * Save a formula.
     *
     * @param formulaDTO the entity to save.
     * @return the persisted entity.
     */
    FormulaDTO save(FormulaDTO formulaDTO);

    /**
     * Updates a formula.
     *
     * @param formulaDTO the entity to update.
     * @return the persisted entity.
     */
    FormulaDTO update(FormulaDTO formulaDTO);

    /**
     * Partially updates a formula.
     *
     * @param formulaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormulaDTO> partialUpdate(FormulaDTO formulaDTO);

    /**
     * Get all the formulas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormulaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" formula.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormulaDTO> findOne(Long id);

    /**
     * Delete the "id" formula.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
