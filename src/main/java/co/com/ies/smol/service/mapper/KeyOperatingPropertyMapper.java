package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.KeyOperatingProperty;
import co.com.ies.smol.service.dto.KeyOperatingPropertyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link KeyOperatingProperty} and its DTO {@link KeyOperatingPropertyDTO}.
 */
@Mapper(componentModel = "spring")
public interface KeyOperatingPropertyMapper extends EntityMapper<KeyOperatingPropertyDTO, KeyOperatingProperty> {}
