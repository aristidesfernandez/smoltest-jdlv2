package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.CurrencyType;
import co.com.ies.smol.service.dto.CurrencyTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CurrencyType} and its DTO {@link CurrencyTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyTypeMapper extends EntityMapper<CurrencyTypeDTO, CurrencyType> {}
