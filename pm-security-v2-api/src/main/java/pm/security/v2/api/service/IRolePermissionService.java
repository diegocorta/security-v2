package pm.security.v2.api.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.springframework.hateoas.EntityModel;

import es.common.service.ICommonService;
import pm.security.v2.api.entity.embedded.key.RolePermissionId;
import pm.security.v2.common.dto.RolePermissionDto;
import pm.security.v2.common.dto.min.PermissionMinDto;

public interface IRolePermissionService
		extends ICommonService<RolePermissionDto, RolePermissionId> {
	
	public Collection<EntityModel<PermissionMinDto>> findPermissionMinByRoleId(Long roleId);
	
	public Collection<EntityModel<PermissionMinDto>> findPermissionMinByRoleIds(Collection<Long> roleIds);
	
	public Collection<EntityModel<RolePermissionDto>> findRolePermissionByRole(Long roleId);
	
	public HashMap<String, Set<String>> findRolePermissionEfective();
	
}