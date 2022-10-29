package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Country;
import co.com.ies.smol.domain.Province;
import co.com.ies.smol.service.dto.CountryDTO;
import co.com.ies.smol.service.dto.ProvinceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Province} and its DTO {@link ProvinceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProvinceMapper extends EntityMapper<ProvinceDTO, Province> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    ProvinceDTO toDto(Province s);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);
}
