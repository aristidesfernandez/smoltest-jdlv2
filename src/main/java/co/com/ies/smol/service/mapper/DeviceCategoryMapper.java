package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.DeviceCategory;
import co.com.ies.smol.service.dto.DeviceCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeviceCategory} and its DTO {@link DeviceCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceCategoryMapper extends EntityMapper<DeviceCategoryDTO, DeviceCategory> {}
