package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Device;
import co.com.ies.smol.domain.DeviceCategory;
import co.com.ies.smol.domain.DeviceType;
import co.com.ies.smol.domain.Formula;
import co.com.ies.smol.domain.Model;
import co.com.ies.smol.service.dto.DeviceCategoryDTO;
import co.com.ies.smol.service.dto.DeviceDTO;
import co.com.ies.smol.service.dto.DeviceTypeDTO;
import co.com.ies.smol.service.dto.FormulaDTO;
import co.com.ies.smol.service.dto.ModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "model", source = "model", qualifiedByName = "modelId")
    @Mapping(target = "deviceCategory", source = "deviceCategory", qualifiedByName = "deviceCategoryId")
    @Mapping(target = "deviceType", source = "deviceType", qualifiedByName = "deviceTypeId")
    @Mapping(target = "formulaHandpay", source = "formulaHandpay", qualifiedByName = "formulaId")
    @Mapping(target = "formulaJackpot", source = "formulaJackpot", qualifiedByName = "formulaId")
    DeviceDTO toDto(Device s);

    @Named("modelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModelDTO toDtoModelId(Model model);

    @Named("deviceCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceCategoryDTO toDtoDeviceCategoryId(DeviceCategory deviceCategory);

    @Named("deviceTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceTypeDTO toDtoDeviceTypeId(DeviceType deviceType);

    @Named("formulaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormulaDTO toDtoFormulaId(Formula formula);
}
