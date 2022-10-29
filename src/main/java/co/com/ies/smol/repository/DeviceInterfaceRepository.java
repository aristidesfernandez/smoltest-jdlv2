package co.com.ies.smol.repository;

import co.com.ies.smol.domain.DeviceInterface;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeviceInterface entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceInterfaceRepository extends JpaRepository<DeviceInterface, Long>, JpaSpecificationExecutor<DeviceInterface> {}
