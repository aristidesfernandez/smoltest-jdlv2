package co.com.ies.smol.repository;

import co.com.ies.smol.domain.CounterType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CounterType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CounterTypeRepository extends JpaRepository<CounterType, String>, JpaSpecificationExecutor<CounterType> {}
