package pm.security.v2.api.service;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;

import es.common.service.ICommonService;
import pm.security.v2.api.entity.embedded.key.UserGroupId;
import pm.security.v2.common.dto.GroupDto;
import pm.security.v2.common.dto.UserDto;
import pm.security.v2.common.dto.UserGroupDto;

public interface IUserGroupService
		extends ICommonService<UserGroupDto, UserGroupId> {
	
	public Collection<EntityModel<GroupDto>> findGroupsByUserId(Long userId);
	
	public Collection<EntityModel<UserGroupDto>> findUserGroupsByUserId(Long userId);
	
	public Collection<EntityModel<UserDto>> findUsersByGroupId(Long groupId);
	
	public Collection<EntityModel<UserGroupDto>> findUserGroupsByGroupId(Long userId);

	
}