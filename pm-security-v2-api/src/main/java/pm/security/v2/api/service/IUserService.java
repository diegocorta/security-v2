package pm.security.v2.api.service;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;

import es.common.service.ICommonMinifiedService;
import pm.security.v2.common.dto.UserDto;
import pm.security.v2.common.dto.UserEffectiveAuthorizationsDto;
import pm.security.v2.common.dto.min.RoleMinDto;
import pm.security.v2.common.dto.min.UserMinDto;

public interface IUserService extends ICommonMinifiedService<UserDto, Long, UserMinDto> {

	/**
	 * Method that receiving a user identifier returns its effective authorizations.
	 * 
	 * The specific authorizations are a combination of all the roles and the
	 * specific permissions that can be granted/revoked despite the user roles
	 * 
	 * @param userId
	 * @return
	 */
	public UserEffectiveAuthorizationsDto findUserEffectivePermissions(String userId);
	
	public Collection<EntityModel<RoleMinDto>> findUserEffectiveRoles(Long userId);

}