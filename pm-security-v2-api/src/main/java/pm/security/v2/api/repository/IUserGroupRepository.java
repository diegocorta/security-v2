package pm.security.v2.api.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import pm.security.v2.api.entity.UserGroup;
import pm.security.v2.api.entity.embedded.key.UserGroupId;

public interface IUserGroupRepository 
		extends JpaRepository<UserGroup, UserGroupId> {
	
	public List<UserGroup> findAllByUserId(Long userId);
	
	public List<UserGroup> findAllByGroupId(Long groupId);

	public List<UserGroup> findAllByGroupIdIn(Set<Long> groupIds);

}
