package pm.security.v2.api.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import es.common.service.BasicMinificableService;
import es.common.util.JoinEntityMap;
import es.common.util.MessageUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import pm.security.v2.api.assembler.RoleAssembler;
import pm.security.v2.api.assembler.UserAssembler;
import pm.security.v2.api.entity.Permission;
import pm.security.v2.api.entity.Role;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.entity.UserGroup;
import pm.security.v2.api.entity.UserGroupPermission;
import pm.security.v2.api.entity.UserGroupRole;
import pm.security.v2.api.repository.IGroupRepository;
import pm.security.v2.api.repository.IGroupRoleRepository;
import pm.security.v2.api.repository.IPermissionRepository;
import pm.security.v2.api.repository.IRoleRepository;
import pm.security.v2.api.repository.IUserGroupPermissionRepository;
import pm.security.v2.api.repository.IUserGroupRepository;
import pm.security.v2.api.repository.IUserGroupRoleRepository;
import pm.security.v2.api.repository.IUserRepository;
import pm.security.v2.api.service.IUserService;
import pm.security.v2.common.dto.UserDto;
import pm.security.v2.common.dto.UserEffectiveAuthorizationsDto;
import pm.security.v2.common.dto.min.RoleMinDto;
import pm.security.v2.common.dto.min.UserMinDto;

@Service
public class UserServiceImpl extends BasicMinificableService<IUserRepository, User, Long, UserDto, UserMinDto, UserAssembler>
		implements IUserService {
	
	private final IUserGroupRoleRepository userGroupRoleRepository;
	private final IGroupRoleRepository groupRoleRepository;
	private final IRoleRepository roleRepository;
	private final IUserGroupRepository userGroupRepository;
	private final IUserGroupPermissionRepository userGroupPermissionRepository;
	private final IPermissionRepository permissionRepository;
	
	public UserServiceImpl(IUserRepository repository,
			IGroupRoleRepository groupRoleRepository,
			IGroupRepository groupRepository,
			IRoleRepository roleRepository,
			IUserGroupRepository userGroupRepository,
			IUserGroupRoleRepository userGroupRoleRepository,
			IUserGroupPermissionRepository userGroupPermissionRepository,
			IPermissionRepository permissionRepository) {
		
		super(User.class, UserMinDto.class, repository, UserAssembler.getInstance());
	
		this.userGroupRoleRepository = userGroupRoleRepository;
		this.roleRepository = roleRepository;
		this.userGroupRepository = userGroupRepository;
		this.groupRoleRepository = groupRoleRepository;
		this.userGroupPermissionRepository = userGroupPermissionRepository;
		this.permissionRepository = permissionRepository;
		
	}

	@Override
	public Map<UserDto, JoinEntityMap> getRelatedEntities(Collection<UserDto> dtos) {
		
		return null;
	}

	@Override
	public void basicDataValidation(Collection<UserDto> dtos) {
		
		for (UserDto dto : dtos) {
			
			User user = new User();
			user.setUsername(dto.getUsername());
			
			// Search a user with the received username
			Optional<User> prevUser = repository.findOne(Example.of(user));
					
		
			// If the previous user is found, and the received user does not have identifier
			// there will be a conflict, two users will be called the same
			if (prevUser.isPresent() && dto.getId() == null) {
				throw new EntityExistsException(
						MessageUtils.entityAlrreadyExistsExceptionMessage(User.DEFAULT_DESCRIPTION));
			
			// If the previous user is found and the received user does have identifier,
			// the identifier must be the same (meaning that an update on the entity is being performed
			// otherwhise, it will be a conflict
			} else if (prevUser.isPresent() && dto.getId() != null) {
				if (!dto.getId().equals(prevUser.get().getId()))
					throw new EntityExistsException(
							MessageUtils.entityAlrreadyExistsExceptionMessage(User.DEFAULT_DESCRIPTION));
			
			// Here there should be no previous user found. So a new entity must be received.
			// As this is a new entity... the idenfitier be null.
			} else {
				Assert.notNull(dto.getId(), MessageUtils.identifierMustBeNull(User.DEFAULT_DESCRIPTION));
			}
			
		}
		
	}

	@Override
	public void createDataValidation(Collection<UserDto> dtos) {

		basicDataValidation(dtos);
	}
	
	@Override
	public Collection<EntityModel<RoleMinDto>> findUserEffectiveRoles(Long userId) {
				
		return RoleAssembler.getInstance()
			.buildMinDtosWithLinksFromEntities(effectiveRoles(userId, true), RoleMinDto.class);

	}

	@Override
	public UserEffectiveAuthorizationsDto findUserEffectivePermissions(
			String username) {
				
		User user = repository.findByUsername(username).orElseThrow(() ->  
				new EntityNotFoundException(
						MessageUtils.entityNotFoundExceptionMessage(User.DEFAULT_DESCRIPTION)));
		
		return findUserEffectivePermissions(user);
	}
	
	private UserEffectiveAuthorizationsDto findUserEffectivePermissions(
			User user) {
		
		Collection<UserGroupPermission> userGroupPermission;
		Set<Long> grantedPermissionIds, revokedPermissionIds;
		
		UserEffectiveAuthorizationsDto effectivePermission = new UserEffectiveAuthorizationsDto();
		
		userGroupPermission = userGroupPermissionRepository.findAllByUserId(user.getId());
		
		grantedPermissionIds = userGroupPermission.stream()
				.filter(ugp -> ugp.getIsGranted())
				.map(ugp -> ugp.getId().getPermissionId())
				.collect(Collectors.toSet());
		
		revokedPermissionIds = userGroupPermission.stream()
				.filter(ugp -> !ugp.getIsGranted())
				.map(ugp -> ugp.getId().getPermissionId())
				.collect(Collectors.toSet());
		
		effectivePermission.setRoles(effectiveRoles(user.getId(), false)
				.stream()
				.map(Role::getCode)
				.collect(Collectors.toSet()));
		
		effectivePermission.setGrantedPermissions(permissionRepository.findAllById(grantedPermissionIds)
				.stream()
				.map(Permission::getCode)
				.collect(Collectors.toSet()));
		
		effectivePermission.setRevokedPermissions(permissionRepository.findAllById(revokedPermissionIds)
				.stream()
				.map(Permission::getCode)
				.collect(Collectors.toSet()));
		
		return effectivePermission;
	}
	
	private List<Role> effectiveRoles(Long userId, boolean effective) {
		
		Collection<UserGroupRole> userGroupRole;
		Collection<UserGroup> userGroup;
		Set<Long> groupIds, roleIds, revokedRoleIds;
		
		userGroupRole = userGroupRoleRepository.findAllByUserId(userId);
		
		// Obtain the groups linked to a user
		userGroup = userGroupRepository.findAllByUserId(userId);
		
		groupIds = userGroupRole
				.stream()
				.map(ugr -> ugr.getId().getGroupId())
				.collect(Collectors.toSet());
		
		groupIds.addAll(userGroup
				.stream()
				.map(ug -> ug.getId().getGroupId())
				.collect(Collectors.toSet()));
		
		// Obtain all roles asociated with the given groups
		roleIds = groupRoleRepository.findAllByGroupIdIn(groupIds)
				.stream()
				.map(gr -> gr.getId().getRoleId())
				.collect(Collectors.toSet());
		
		revokedRoleIds = userGroupRole
				.stream()
				.filter(ugr -> !ugr.getIsGranted())
				.map(ugr -> ugr.getId().getRoleId())
				.collect(Collectors.toSet());
		
		roleIds.addAll(userGroupRole
				.stream()
				.map(ugr -> ugr.getId().getRoleId())
				.collect(Collectors.toSet()));

		roleIds.removeIf(roleId -> revokedRoleIds.contains(roleId));

		
		if (effective) {
			
			var response = roleRepository.findAllById(roleIds);
			
			response.addAll(roleRepository.findAllAncestorsRecursive(roleIds)
					.stream()
					.collect(Collectors.toList()));
						
			return 	response.stream().distinct().collect(Collectors.toList());
		} else {
			return roleRepository.findAllById(roleIds);

		}
		
	}
	
	
	
}
 