package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Device;
import co.com.ies.smol.domain.DeviceEstablishment;
import co.com.ies.smol.service.dto.DeviceDTO;
import co.com.ies.smol.service.dto.DeviceEstablishmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeviceEstablishment} and its DTO {@link DeviceEstablishmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceEstablishmentMapper extends EntityMapper<DeviceEstablishmentDTO, DeviceEstablishment> {
    @Mapping(target = "device", source = "device", qualifiedByName = "deviceId")
    DeviceEstablishmentDTO toDto(DeviceEstablishment s);

    @Named("deviceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoDeviceId(Device device);
}
