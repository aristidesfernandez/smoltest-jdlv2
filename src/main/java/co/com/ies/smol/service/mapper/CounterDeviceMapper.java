package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.CounterDevice;
import co.com.ies.smol.domain.Device;
import co.com.ies.smol.service.dto.CounterDeviceDTO;
import co.com.ies.smol.service.dto.DeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CounterDevice} and its DTO {@link CounterDeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CounterDeviceMapper extends EntityMapper<CounterDeviceDTO, CounterDevice> {
    @Mapping(target = "device", source = "device", qualifiedByName = "deviceId")
    CounterDeviceDTO toDto(CounterDevice s);

    @Named("deviceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoDeviceId(Device device);
}
