package co.com.ies.smol.repository;

import co.com.ies.smol.domain.CounterEvent;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CounterEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CounterEventRepository extends JpaRepository<CounterEvent, UUID>, JpaSpecificationExecutor<CounterEvent> {}
