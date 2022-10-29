package co.com.ies.smol.repository;

import co.com.ies.smol.domain.Isle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Isle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IsleRepository extends JpaRepository<Isle, Long>, JpaSpecificationExecutor<Isle> {}
