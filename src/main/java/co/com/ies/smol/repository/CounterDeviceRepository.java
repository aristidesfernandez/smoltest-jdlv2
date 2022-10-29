package co.com.ies.smol.repository;

import co.com.ies.smol.domain.CounterDevice;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CounterDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CounterDeviceRepository extends JpaRepository<CounterDevice, UUID>, JpaSpecificationExecutor<CounterDevice> {}
