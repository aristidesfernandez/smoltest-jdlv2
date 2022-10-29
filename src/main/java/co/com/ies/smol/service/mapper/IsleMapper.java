package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Isle;
import co.com.ies.smol.service.dto.IsleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Isle} and its DTO {@link IsleDTO}.
 */
@Mapper(componentModel = "spring")
public interface IsleMapper extends EntityMapper<IsleDTO, Isle> {}
