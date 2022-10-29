package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.CounterEvent;
import co.com.ies.smol.domain.EventDevice;
import co.com.ies.smol.service.dto.CounterEventDTO;
import co.com.ies.smol.service.dto.EventDeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CounterEvent} and its DTO {@link CounterEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface CounterEventMapper extends EntityMapper<CounterEventDTO, CounterEvent> {
    @Mapping(target = "eventDevice", source = "eventDevice", qualifiedByName = "eventDeviceId")
    CounterEventDTO toDto(CounterEvent s);

    @Named("eventDeviceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDeviceDTO toDtoEventDeviceId(EventDevice eventDevice);
}
