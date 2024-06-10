package pm.security.v2.api.service.impl;

import java.util.Collection;
import java.util.HashSet;
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
import pm.security.v2.api.assembler.RoleAssembler;
import pm.security.v2.api.assembler.UserAssembler;
import pm.security.v2.api.entity.Role;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.entity.UserGroupRole;
import pm.security.v2.api.repository.IGroupRoleRepository;
import pm.security.v2.api.repository.IRoleRepository;
import pm.security.v2.api.repository.IUserGroupRepository;
import pm.security.v2.api.repository.IUserGroupRoleRepository;
import pm.security.v2.api.repository.IUserRepository;
import pm.security.v2.api.service.IRoleService;
import pm.security.v2.common.dto.RoleDto;
import pm.security.v2.common.dto.min.RoleMinDto;
import pm.security.v2.common.dto.min.UserMinDto;

@Service
public class RoleServiceImpl extends BasicMinificableService<IRoleRepository, Role, Long, RoleDto, RoleMinDto, RoleAssembler>
		implements IRoleService {
	
	private final IUserGroupRoleRepository userGroupRoleRepository;
	private final IUserGroupRepository userGroupRepository;
	private final IGroupRoleRepository groupRoleRepository;
	private final IUserRepository userRepository;
		
	public RoleServiceImpl(IRoleRepository repository,
			IUserGroupRoleRepository userGroupRoleRepository,
			IUserGroupRepository userGroupRepository,
			IGroupRoleRepository groupRoleRepository,
			IUserRepository userRepository) {
		super(Role.class, RoleMinDto.class, repository, RoleAssembler.getInstance());
		
		this.userGroupRoleRepository = userGroupRoleRepository;
		this.userGroupRepository = userGroupRepository;
		this.userRepository = userRepository;
		this.groupRoleRepository = groupRoleRepository;
		
	}

	@Override
	public Map<RoleDto, JoinEntityMap> getRelatedEntities(Collection<RoleDto> dtos) {
		
		return null;
	}

	@Override
	public void basicDataValidation(Collection<RoleDto> dtos) {
		
		for (RoleDto dto : dtos) {
			
			Role role = new Role();
			role.setName(dto.getName());
			
			Optional<Role> prevRole = repository.findOne(Example.of(role));
			boolean nameDuplicated = false;
			
			if (nameDuplicated) {
				
				Assert.isNull(dto.getId(), MessageUtils.identifierMustBeNull(Role.DEFAULT_DESCRIPTION));
				if (prevRole.isPresent()) nameDuplicated = true;
				
			} else {
				
				Assert.notNull(dto.getId(), MessageUtils.identifierMustNotBeNull(Role.DEFAULT_DESCRIPTION));
				if (prevRole.isPresent() && !prevRole.get().getId().equals(dto.getId()))
					nameDuplicated = true;
				
			}
			
			if (nameDuplicated) {
				throw new EntityExistsException(MessageUtils.entityAlrreadyExistsExceptionMessage(Role.DEFAULT_DESCRIPTION));
			}
		}
		
	}

	@Override
	public void createDataValidation(Collection<RoleDto> dtos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<EntityModel<UserMinDto>> findUserOfRoles(Long roleId) {
		
		Set<Role> role = new HashSet<>();
		Set<Long> roleIds = new HashSet<>();
		
		role.add(repository.findById(roleId).get());
		role.addAll(repository.findAllDescendantsRecursive(roleId));
		
		roleIds = role
				.stream()
				.map(Role::getId)
				.collect(Collectors.toSet());
		
		Set<UserGroupRole> userGroupRoles = userGroupRoleRepository.findAllByRoleIdIn(roleIds)
				.stream()
				.collect(Collectors.toSet());
		
		Set<Long> groupIds = groupRoleRepository.findAllByRoleIdIn(roleIds)
			.stream()
			.map(groupRole -> groupRole.getGroup().getId())
			.collect(Collectors.toSet());
		
		Set<Long> userIds = userGroupRepository.findAllByGroupIdIn(groupIds)
				.stream()
				.map(ug -> ug.getGroup().getId())
				.collect(Collectors.toSet());
		
		Set<User> users = userRepository.findAllById(userIds)
				.stream()
				.collect(Collectors.toSet());
		
		for (UserGroupRole userGroupRole : userGroupRoles) {
			
			if (userGroupRole.getIsGranted() && !userIds.contains(userGroupRole.getUser().getId())) {
				
				users.add(userRepository.findById(userGroupRole.getUser().getId()).get());
				userIds.add(userGroupRole.getUser().getId());
				
			} else if (!userGroupRole.getIsGranted()) {
				
				users.remove(userRepository.findById(userGroupRole.getUser().getId()).get());
			}
		}
				
		return UserAssembler.getInstance()
				.buildMinDtosWithLinksFromEntities(users, UserMinDto.class);
	}
	
	
	
}
 