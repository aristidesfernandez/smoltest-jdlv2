package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.Formula;
import co.com.ies.smol.service.dto.FormulaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formula} and its DTO {@link FormulaDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormulaMapper extends EntityMapper<FormulaDTO, Formula> {}
