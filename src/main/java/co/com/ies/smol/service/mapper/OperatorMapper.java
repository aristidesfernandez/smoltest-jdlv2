package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Municipality;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.service.dto.MunicipalityDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Operator} and its DTO {@link OperatorDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperatorMapper extends EntityMapper<OperatorDTO, Operator> {
    @Mapping(target = "municipality", source = "municipality", qualifiedByName = "municipalityId")
    OperatorDTO toDto(Operator s);

    @Named("municipalityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MunicipalityDTO toDtoMunicipalityId(Municipality municipality);
}
