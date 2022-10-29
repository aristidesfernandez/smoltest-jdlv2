package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.DeviceEstablishment;
import co.com.ies.smol.domain.EventDevice;
import co.com.ies.smol.domain.EventType;
import co.com.ies.smol.service.dto.DeviceEstablishmentDTO;
import co.com.ies.smol.service.dto.EventDeviceDTO;
import co.com.ies.smol.service.dto.EventTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventDevice} and its DTO {@link EventDeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventDeviceMapper extends EntityMapper<EventDeviceDTO, EventDevice> {
    @Mapping(target = "deviceEstablishment", source = "deviceEstablishment", qualifiedByName = "deviceEstablishmentId")
    @Mapping(target = "eventType", source = "eventType", qualifiedByName = "eventTypeId")
    EventDeviceDTO toDto(EventDevice s);

    @Named("deviceEstablishmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceEstablishmentDTO toDtoDeviceEstablishmentId(DeviceEstablishment deviceEstablishment);

    @Named("eventTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventTypeDTO toDtoEventTypeId(EventType eventType);
}
