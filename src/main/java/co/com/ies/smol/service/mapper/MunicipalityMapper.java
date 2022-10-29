package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Municipality;
import co.com.ies.smol.domain.Province;
import co.com.ies.smol.service.dto.MunicipalityDTO;
import co.com.ies.smol.service.dto.ProvinceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Municipality} and its DTO {@link MunicipalityDTO}.
 */
@Mapper(componentModel = "spring")
public interface MunicipalityMapper extends EntityMapper<MunicipalityDTO, Municipality> {
    @Mapping(target = "province", source = "province", qualifiedByName = "provinceId")
    MunicipalityDTO toDto(Municipality s);

    @Named("provinceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinceDTO toDtoProvinceId(Province province);
}
