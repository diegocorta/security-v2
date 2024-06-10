package pm.security.v2.api.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
import pm.security.v2.api.assembler.UserGroupPermissionAssembler;
import pm.security.v2.api.entity.Group;
import pm.security.v2.api.entity.Permission;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.entity.UserGroupPermission;
import pm.security.v2.api.entity.embedded.key.UserGroupPermissionId;
import pm.security.v2.api.repository.IGroupRepository;
import pm.security.v2.api.repository.IPermissionRepository;
import pm.security.v2.api.repository.IUserGroupPermissionRepository;
import pm.security.v2.api.repository.IUserRepository;
import pm.security.v2.api.service.IUserGroupPermissionService;
import pm.security.v2.common.dto.UserGroupPermissionDto;

@Service
public class UserGroupPermissionServiceImpl extends BasicService<IUserGroupPermissionRepository, UserGroupPermission, UserGroupPermissionId, UserGroupPermissionDto, UserGroupPermissionAssembler>
		implements IUserGroupPermissionService {
	
	private final IUserRepository userRepository;
	private final IGroupRepository groupRepository;
	private final IPermissionRepository permissionRepository;
	
	public UserGroupPermissionServiceImpl(IUserGroupPermissionRepository repository,
			IUserRepository userRepository, IGroupRepository groupRepository,
			IPermissionRepository permissionRepository) {
		
		super(UserGroupPermission.class, repository, UserGroupPermissionAssembler.getInstance());
		
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
		this.permissionRepository = permissionRepository;
		
	}
	
	@Override
	public Map<UserGroupPermissionDto, JoinEntityMap> getRelatedEntities(Collection<UserGroupPermissionDto> dtos) {
		
		ConcurrentHashMap<UserGroupPermissionDto, JoinEntityMap> result = new ConcurrentHashMap<>();
		
		// Obtaining all the identifier of users and groups
		List<Long> userIds = dtos.parallelStream()
				.map(UserGroupPermissionDto::getUserId)
				.distinct()
				.toList();
		
		List<Long> groupIds = dtos.parallelStream()
				.map(UserGroupPermissionDto::getGroupId)
				.distinct()
				.toList();
		
		List<Long> permissionIds = dtos.parallelStream()
				.map(UserGroupPermissionDto::getPermissionId)
				.distinct()
				.toList();
		
		// Search every entity located
		Map<Long, User> userMap = userRepository.findAllById(userIds)
				.stream()
				.collect(Collectors.toMap(User::getId, Function.identity()));
		
		Map<Long, Group> groupMap = groupRepository.findAllById(groupIds)
				.stream()
				.collect(Collectors.toMap(Group::getId, Function.identity()));
		
		Map<Long, Permission> permissionMap = permissionRepository.findAllById(permissionIds)
				.stream()
				.collect(Collectors.toMap(Permission::getId, Function.identity()));
		
		
		dtos.parallelStream().forEach(dto -> {
				
			User user = userMap.get(dto.getUserId());
			Group group = groupMap.get(dto.getGroupId());
			Permission permission = permissionMap.get(dto.getPermissionId());

			if (user == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(User.DEFAULT_DESCRIPTION));
			
						
			if (group == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(Group.DEFAULT_DESCRIPTION));
			
			if (permission == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(Permission.DEFAULT_DESCRIPTION));
			
			
			Collection<Pair<String, Object>> joinEntityList = List.of(
					Pair.of(UserGroupPermission.GROUP, group),
					Pair.of(UserGroupPermission.USER, user),
					Pair.of(UserGroupPermission.PERMISSION, permission)
			);

			result.put(dto, JoinEntityMap.from(joinEntityList));
			
		});
		
		return result;
		
	}
	
	
	@Override
	public void basicDataValidation(Collection<UserGroupPermissionDto> dtos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDataValidation(Collection<UserGroupPermissionDto> dtos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<EntityModel<UserGroupPermissionDto>> findUserGroupPermissionByUserIdAndGroupId(Long userId,
			Long groupId) {
		
		return assembler.buildDtosWithLinksFromEntities(
				repository.findAllByUserIdAndGroupId(userId, groupId));
		
	}
	
}
 