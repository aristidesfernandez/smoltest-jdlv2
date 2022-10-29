package co.com.ies.smol.repository;

import co.com.ies.smol.domain.CurrencyType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CurrencyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Long>, JpaSpecificationExecutor<CurrencyType> {}
