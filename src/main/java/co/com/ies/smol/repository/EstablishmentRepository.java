package co.com.ies.smol.repository;

import co.com.ies.smol.domain.Establishment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Establishment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long>, JpaSpecificationExecutor<Establishment> {}
