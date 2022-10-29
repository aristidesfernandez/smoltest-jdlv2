package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Establishment;
import co.com.ies.smol.domain.Municipality;
import co.com.ies.smol.domain.Operator;
import co.com.ies.smol.service.dto.EstablishmentDTO;
import co.com.ies.smol.service.dto.MunicipalityDTO;
import co.com.ies.smol.service.dto.OperatorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Establishment} and its DTO {@link EstablishmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstablishmentMapper extends EntityMapper<EstablishmentDTO, Establishment> {
    @Mapping(target = "operator", source = "operator", qualifiedByName = "operatorId")
    @Mapping(target = "municipality", source = "municipality", qualifiedByName = "municipalityId")
    EstablishmentDTO toDto(Establishment s);

    @Named("operatorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OperatorDTO toDtoOperatorId(Operator operator);

    @Named("municipalityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MunicipalityDTO toDtoMunicipalityId(Municipality municipality);
}
