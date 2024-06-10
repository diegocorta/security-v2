package pm.security.v2.api.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pm.security.v2.api.entity.GroupRole;
import pm.security.v2.api.entity.embedded.key.GroupRoleId;

public interface IGroupRoleRepository 
		extends JpaRepository< GroupRole, GroupRoleId> {
	
	public Collection<GroupRole> findAllByGroupId(Long groupId);
	
	public Collection<GroupRole> findAllByRoleId(Long roleId);
	
	public Collection<GroupRole> findAllByGroupIdIn(Collection<Long> groupIds);

	public Collection<GroupRole> findAllByRoleIdIn(Collection<Long> roleIds);

}
