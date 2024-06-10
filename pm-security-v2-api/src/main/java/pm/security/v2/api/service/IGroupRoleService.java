package pm.security.v2.api.service;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;

import es.common.service.ICommonService;
import pm.security.v2.api.entity.embedded.key.GroupRoleId;
import pm.security.v2.common.dto.GroupRoleDto;
import pm.security.v2.common.dto.min.GroupMinDto;
import pm.security.v2.common.dto.min.RoleMinDto;

public interface IGroupRoleService
		extends ICommonService<GroupRoleDto, GroupRoleId> {
	
	public Collection<EntityModel<RoleMinDto>> findRolesMinOfGroup(Long groupId);
	
	public Collection<EntityModel<GroupMinDto>> findGroupMinOfRole(Long roleId);
	
	public Collection<EntityModel<GroupRoleDto>> findGroupRolesByGroup(Long groupId);

}