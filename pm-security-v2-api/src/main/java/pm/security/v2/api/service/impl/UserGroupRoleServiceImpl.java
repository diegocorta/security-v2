package pm.security.v2.api.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.util.Pair;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import es.common.service.BasicService;
import es.common.util.JoinEntityMap;
import es.common.util.MessageUtils;
import jakarta.persistence.EntityNotFoundException;
import pm.security.v2.api.assembler.RoleAssembler;
import pm.security.v2.api.assembler.UserGroupRoleAssembler;
import pm.security.v2.api.entity.Group;
import pm.security.v2.api.entity.Role;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.entity.UserGroupRole;
import pm.security.v2.api.entity.embedded.key.UserGroupRoleId;
import pm.security.v2.api.repository.IGroupRepository;
import pm.security.v2.api.repository.IRoleRepository;
import pm.security.v2.api.repository.IUserGroupRoleRepository;
import pm.security.v2.api.repository.IUserRepository;
import pm.security.v2.api.service.IUserGroupRoleService;
import pm.security.v2.common.dto.UserGroupRoleDto;
import pm.security.v2.common.dto.min.RoleMinDto;

@Service
public class UserGroupRoleServiceImpl extends BasicService<IUserGroupRoleRepository, UserGroupRole, UserGroupRoleId, UserGroupRoleDto, UserGroupRoleAssembler>
		implements IUserGroupRoleService {
	
	private final IRoleRepository roleRepository;
	private final IUserRepository userRepository;
	private final IGroupRepository groupRepository;
	
	private final RoleAssembler roleAssembler;
	
	public UserGroupRoleServiceImpl(IUserGroupRoleRepository repository,
			IRoleRepository roleRepository, IUserRepository userRepository,
			IGroupRepository groupRepository) {
		
		super(UserGroupRole.class, repository, UserGroupRoleAssembler.getInstance());
		
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;

		this.roleAssembler = RoleAssembler.getInstance();
		
	}
	
	@Override
	public Map<UserGroupRoleDto, JoinEntityMap> getRelatedEntities(Collection<UserGroupRoleDto> dtos) {
		
		ConcurrentHashMap<UserGroupRoleDto, JoinEntityMap> result = new ConcurrentHashMap<>();
		
		// Obtaining all the identifier of users and groups
		List<Long> userIds = dtos.parallelStream()
				.map(UserGroupRoleDto::getUserId)
				.distinct()
				.toList();
		
		List<Long> groupIds = dtos.parallelStream()
				.map(UserGroupRoleDto::getGroupId)
				.distinct()
				.toList();
		
		List<Long> roleIds = dtos.parallelStream()
				.map(UserGroupRoleDto::getRoleId)
				.distinct()
				.toList();
		
		// Search every entity located
		Map<Long, User> userMap = userRepository.findAllById(userIds)
				.stream()
				.collect(Collectors.toMap(User::getId, Function.identity()));
		
		Map<Long, Group> groupMap = groupRepository.findAllById(groupIds)
				.stream()
				.collect(Collectors.toMap(Group::getId, Function.identity()));
		
		Map<Long, Role> roleMap = roleRepository.findAllById(roleIds)
				.stream()
				.collect(Collectors.toMap(Role::getId, Function.identity()));
		
		
		dtos.parallelStream().forEach(dto -> {
				
			User user = userMap.get(dto.getUserId());
			Group group = groupMap.get(dto.getGroupId());
			Role role = roleMap.get(dto.getRoleId());

			if (user == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(User.DEFAULT_DESCRIPTION));
			
						
			if (group == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(Group.DEFAULT_DESCRIPTION));
			
			if (role == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(Role.DEFAULT_DESCRIPTION));
			
			
			Collection<Pair<String, Object>> joinEntityList = List.of(
					Pair.of(UserGroupRole.GROUP, group),
					Pair.of(UserGroupRole.USER, user),
					Pair.of(UserGroupRole.ROLE, role)
			);

			result.put(dto, JoinEntityMap.from(joinEntityList));
			
		});
		
		return result;
		
		
	}
	
	@Override
	public void basicDataValidation(Collection<UserGroupRoleDto> dtos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDataValidation(Collection<UserGroupRoleDto> dtos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<EntityModel<RoleMinDto>> findRoleMinByUserId(Long userId) {
		
		Set<Long> roleIds = repository.findAllByUserId(userId)
				.stream()
				.map(rp -> rp.getId().getRoleId())
				.collect(Collectors.toSet());
		
		return roleRepository.findAllById(roleIds)
				.stream()
				.map(role -> roleAssembler.buildMinDtoWithLinksFromEntity(role, RoleMinDto.class))
				.collect(Collectors.toCollection(ArrayList::new));

	}

	@Override
	public Collection<EntityModel<RoleMinDto>> findRoleMinByUserIdAndGroupId(Long userId, Long groupId) {
		
		Set<Long> roleIds = repository.findAllByUserIdAndGroupId(userId, groupId)
				.stream()
				.map(rp -> rp.getId().getRoleId())
				.collect(Collectors.toSet());
		
		return roleRepository.findAllById(roleIds)
				.stream()
				.map(role -> roleAssembler.buildMinDtoWithLinksFromEntity(role, RoleMinDto.class))
				.collect(Collectors.toCollection(ArrayList::new));

	}

	@Override
	public Collection<EntityModel<UserGroupRoleDto>> findUserGroupRoleByUserIdAndGroupId(Long userId, Long groupId) {
		
		return assembler.buildDtosWithLinksFromEntities(
				repository.findAllByUserIdAndGroupId(userId, groupId));
		
	}
	
}
 