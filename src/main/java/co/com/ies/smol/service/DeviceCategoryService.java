package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.DeviceCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.DeviceCategory}.
 */
public interface DeviceCategoryService {
    /**
     * Save a deviceCategory.
     *
     * @param deviceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    DeviceCategoryDTO save(DeviceCategoryDTO deviceCategoryDTO);

    /**
     * Updates a deviceCategory.
     *
     * @param deviceCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    DeviceCategoryDTO update(DeviceCategoryDTO deviceCategoryDTO);

    /**
     * Partially updates a deviceCategory.
     *
     * @param deviceCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeviceCategoryDTO> partialUpdate(DeviceCategoryDTO deviceCategoryDTO);

    /**
     * Get all the deviceCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" deviceCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeviceCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" deviceCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
