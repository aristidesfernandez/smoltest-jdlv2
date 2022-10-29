package co.com.ies.smol.service.mapper;

import co.com.ies.smol.domain.UserAccess;
import co.com.ies.smol.service.dto.UserAccessDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAccess} and its DTO {@link UserAccessDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAccessMapper extends EntityMapper<UserAccessDTO, UserAccess> {}
