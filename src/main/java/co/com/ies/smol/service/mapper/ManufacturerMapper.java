package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Manufacturer;
import co.com.ies.smol.service.dto.ManufacturerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Manufacturer} and its DTO {@link ManufacturerDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManufacturerMapper extends EntityMapper<ManufacturerDTO, Manufacturer> {}
