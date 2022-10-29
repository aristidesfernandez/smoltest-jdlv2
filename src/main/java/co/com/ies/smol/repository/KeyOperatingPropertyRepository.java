package co.com.ies.smol.repository;

import co.com.ies.smol.domain.KeyOperatingProperty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the KeyOperatingProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeyOperatingPropertyRepository
    extends JpaRepository<KeyOperatingProperty, Long>, JpaSpecificationExecutor<KeyOperatingProperty> {}
