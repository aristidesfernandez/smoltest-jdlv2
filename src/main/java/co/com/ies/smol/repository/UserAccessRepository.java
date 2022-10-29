package co.com.ies.smol.repository;

import co.com.ies.smol.domain.UserAccess;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserAccess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, Long>, JpaSpecificationExecutor<UserAccess> {}
