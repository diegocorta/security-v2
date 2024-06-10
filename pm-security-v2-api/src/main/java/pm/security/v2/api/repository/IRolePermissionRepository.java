package pm.security.v2.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pm.security.v2.api.entity.RolePermission;
import pm.security.v2.api.entity.embedded.key.RolePermissionId;

public interface IRolePermissionRepository 
		extends JpaRepository<RolePermission, RolePermissionId> {

	public Collection<RolePermission> findAllByRoleId(Long roleId);
	
	public Collection<RolePermission> findAllByRoleIdIn(Collection<Long> roleIds);

}
