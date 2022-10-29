package co.com.ies.smol.repository;

import co.com.ies.smol.domain.EventDevice;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventDeviceRepository extends JpaRepository<EventDevice, UUID>, JpaSpecificationExecutor<EventDevice> {}
