package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Device;
import co.com.ies.smol.domain.DeviceInterface;
import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.service.dto.DeviceDTO;
import co.com.ies.smol.service.dto.DeviceInterfaceDTO;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeviceInterface} and its DTO {@link DeviceInterfaceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceInterfaceMapper extends EntityMapper<DeviceInterfaceDTO, DeviceInterface> {
    @Mapping(target = "device", source = "device", qualifiedByName = "deviceId")
    @Mapping(target = "interfaceBoard", source = "interfaceBoard", qualifiedByName = "interfaceBoardId")
    DeviceInterfaceDTO toDto(DeviceInterface s);

    @Named("deviceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoDeviceId(Device device);

    @Named("interfaceBoardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InterfaceBoardDTO toDtoInterfaceBoardId(InterfaceBoard interfaceBoard);
}
