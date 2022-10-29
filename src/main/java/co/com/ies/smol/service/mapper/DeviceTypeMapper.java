package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.DeviceType;
import co.com.ies.smol.service.dto.DeviceTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeviceType} and its DTO {@link DeviceTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceTypeMapper extends EntityMapper<DeviceTypeDTO, DeviceType> {}
