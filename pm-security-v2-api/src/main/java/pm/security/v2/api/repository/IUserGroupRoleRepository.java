package pm.security.v2.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pm.security.v2.api.entity.UserGroupRole;
import pm.security.v2.api.entity.embedded.key.UserGroupRoleId;

public interface IUserGroupRoleRepository 
		extends JpaRepository<UserGroupRole, UserGroupRoleId> {

	public Collection<UserGroupRole> findAllByRoleId(Long roleId);
	
	public Collection<UserGroupRole> findAllByRoleIdIn(Collection<Long> roleIds);
	
	public Collection<UserGroupRole> findAllByUserId(Long userId);
	
	public Collection<UserGroupRole> findAllByUserIdAndGroupId(Long userId, Long groupId);
		
}
