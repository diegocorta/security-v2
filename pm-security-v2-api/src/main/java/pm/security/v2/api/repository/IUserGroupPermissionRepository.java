package pm.security.v2.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pm.security.v2.api.entity.UserGroupPermission;
import pm.security.v2.api.entity.embedded.key.UserGroupPermissionId;

public interface IUserGroupPermissionRepository 
		extends JpaRepository<UserGroupPermission, UserGroupPermissionId> {

	public Collection<UserGroupPermission> findAllByUserId(Long userId);
	
	public Collection<UserGroupPermission> findAllByUserIdAndGroupId(Long userId, Long groupId);

}
