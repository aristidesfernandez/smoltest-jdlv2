package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.InterfaceBoard;
import co.com.ies.smol.service.dto.InterfaceBoardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InterfaceBoard} and its DTO {@link InterfaceBoardDTO}.
 */
@Mapper(componentModel = "spring")
public interface InterfaceBoardMapper extends EntityMapper<InterfaceBoardDTO, InterfaceBoard> {}
