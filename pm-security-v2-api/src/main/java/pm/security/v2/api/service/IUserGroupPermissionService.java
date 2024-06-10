package pm.security.v2.api.service;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;

import es.common.service.ICommonService;
import pm.security.v2.api.entity.embedded.key.UserGroupPermissionId;
import pm.security.v2.common.dto.UserGroupPermissionDto;

public interface IUserGroupPermissionService
		extends ICommonService<UserGroupPermissionDto, UserGroupPermissionId> {
	
	public Collection<EntityModel<UserGroupPermissionDto>> findUserGroupPermissionByUserIdAndGroupId(Long userId, Long groupId);

}