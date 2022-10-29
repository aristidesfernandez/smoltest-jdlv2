package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Formula;
import co.com.ies.smol.domain.Manufacturer;
import co.com.ies.smol.domain.Model;
import co.com.ies.smol.service.dto.FormulaDTO;
import co.com.ies.smol.service.dto.ManufacturerDTO;
import co.com.ies.smol.service.dto.ModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Model} and its DTO {@link ModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModelMapper extends EntityMapper<ModelDTO, Model> {
    @Mapping(target = "manufacturer", source = "manufacturer", qualifiedByName = "manufacturerId")
    @Mapping(target = "formula", source = "formula", qualifiedByName = "formulaId")
    ModelDTO toDto(Model s);

    @Named("manufacturerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ManufacturerDTO toDtoManufacturerId(Manufacturer manufacturer);

    @Named("formulaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormulaDTO toDtoFormulaId(Formula formula);
}
