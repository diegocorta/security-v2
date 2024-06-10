package pm.security.v2.api.service;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;

import es.common.service.ICommonMinifiedService;
import pm.security.v2.common.dto.RoleDto;
import pm.security.v2.common.dto.min.RoleMinDto;
import pm.security.v2.common.dto.min.UserMinDto;

public interface IRoleService
		extends ICommonMinifiedService<RoleDto, Long, RoleMinDto> {

	Collection<EntityModel<UserMinDto>> findUserOfRoles(Long roleId);
	
}