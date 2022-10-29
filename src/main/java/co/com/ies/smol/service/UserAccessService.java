package co.com.ies.smol.service;

import co.com.ies.smol.service.dto.UserAccessDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.smol.domain.UserAccess}.
 */
public interface UserAccessService {
    /**
     * Save a userAccess.
     *
     * @param userAccessDTO the entity to save.
     * @return the persisted entity.
     */
    UserAccessDTO save(UserAccessDTO userAccessDTO);

    /**
     * Updates a userAccess.
     *
     * @param userAccessDTO the entity to update.
     * @return the persisted entity.
     */
    UserAccessDTO update(UserAccessDTO userAccessDTO);

    /**
     * Partially updates a userAccess.
     *
     * @param userAccessDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAccessDTO> partialUpdate(UserAccessDTO userAccessDTO);

    /**
     * Get all the userAccesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAccessDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userAccess.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAccessDTO> findOne(Long id);

    /**
     * Delete the "id" userAccess.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
