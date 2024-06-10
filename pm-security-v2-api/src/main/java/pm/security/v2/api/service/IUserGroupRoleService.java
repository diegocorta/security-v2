package pm.security.v2.api.service;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;

import es.common.service.ICommonService;
import pm.security.v2.api.entity.embedded.key.UserGroupRoleId;
import pm.security.v2.common.dto.UserGroupRoleDto;
import pm.security.v2.common.dto.min.RoleMinDto;

public interface IUserGroupRoleService
		extends ICommonService<UserGroupRoleDto, UserGroupRoleId> {
	
	public Collection<EntityModel<RoleMinDto>> findRoleMinByUserId(Long userId);
	
	public Collection<EntityModel<RoleMinDto>> findRoleMinByUserIdAndGroupId(Long userId, Long groupId);
	
	public Collection<EntityModel<UserGroupRoleDto>> findUserGroupRoleByUserIdAndGroupId(Long userId, Long groupId);
		
}