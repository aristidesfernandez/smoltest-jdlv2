package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.CounterType;
import co.com.ies.smol.service.dto.CounterTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CounterType} and its DTO {@link CounterTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CounterTypeMapper extends EntityMapper<CounterTypeDTO, CounterType> {}
