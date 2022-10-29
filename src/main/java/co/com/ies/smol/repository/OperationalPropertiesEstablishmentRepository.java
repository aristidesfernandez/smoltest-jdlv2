package co.com.ies.smol.repository;

import co.com.ies.smol.domain.OperationalPropertiesEstablishment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OperationalPropertiesEstablishment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperationalPropertiesEstablishmentRepository
    extends JpaRepository<OperationalPropertiesEstablishment, Long>, JpaSpecificationExecutor<OperationalPropertiesEstablishment> {}
