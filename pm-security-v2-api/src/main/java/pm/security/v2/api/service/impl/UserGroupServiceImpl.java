package pm.security.v2.api.service.impl;

import java.util.ArrayList;
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
import pm.security.v2.api.assembler.GroupAssembler;
import pm.security.v2.api.assembler.UserAssembler;
import pm.security.v2.api.assembler.UserGroupAssembler;
import pm.security.v2.api.entity.Group;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.entity.UserGroup;
import pm.security.v2.api.entity.embedded.key.UserGroupId;
import pm.security.v2.api.repository.IGroupRepository;
import pm.security.v2.api.repository.IUserGroupRepository;
import pm.security.v2.api.repository.IUserRepository;
import pm.security.v2.api.service.IUserGroupService;
import pm.security.v2.common.dto.GroupDto;
import pm.security.v2.common.dto.UserDto;
import pm.security.v2.common.dto.UserGroupDto;

@Service
public class UserGroupServiceImpl extends BasicService<IUserGroupRepository, UserGroup, UserGroupId, UserGroupDto, UserGroupAssembler>
		implements IUserGroupService {
	
	private final IUserRepository userRepository;
	private final IGroupRepository groupRepository;
	
	private final GroupAssembler groupAssembler;
	private final UserAssembler userAssembler;
	
	public UserGroupServiceImpl(IUserGroupRepository repository,
			IUserRepository userRepository, IGroupRepository groupRepository) {
		super(UserGroup.class, repository, UserGroupAssembler.getInstance());
		
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
		
		this.groupAssembler = GroupAssembler.getInstance();
		this.userAssembler = UserAssembler.getInstance();
		
	}
	
	@Override
	public Map<UserGroupDto, JoinEntityMap> getRelatedEntities(Collection<UserGroupDto> dtos) {
		
		ConcurrentHashMap<UserGroupDto, JoinEntityMap> result = new ConcurrentHashMap<>();
		
		// Obtaining all the identifier of users and groups
		List<Long> userIds = dtos.parallelStream()
				.map(UserGroupDto::getUserId)
				.distinct()
				.toList();
		
		List<Long> groupIds = dtos.parallelStream()
				.map(UserGroupDto::getGroupId)
				.distinct()
				.toList();
		
		// Search every entity located
		Map<Long, User> userMap = userRepository.findAllById(userIds)
				.stream()
				.collect(Collectors.toMap(User::getId, Function.identity()));
		
		Map<Long, Group> groupMap = groupRepository.findAllById(groupIds)
				.stream()
				.collect(Collectors.toMap(Group::getId, Function.identity()));
		
		
		dtos.parallelStream().forEach(dto -> {
				
			User user = userMap.get(dto.getUserId());
			Group group = groupMap.get(dto.getGroupId());

			if (user == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(User.DEFAULT_DESCRIPTION));
			
						
			if (group == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(Group.DEFAULT_DESCRIPTION));
			
			
			Collection<Pair<String, Object>> joinEntityList = List.of(
					Pair.of(UserGroup.GROUP, group),
					Pair.of(UserGroup.USER, user)
			);

			result.put(dto, JoinEntityMap.from(joinEntityList));
			
		});
		
		return result;
		
	}
	
	@Override
	public void basicDataValidation(Collection<UserGroupDto> dtos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDataValidation(Collection<UserGroupDto> dtos) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	@Override
	public Collection<EntityModel<GroupDto>> findGroupsByUserId(Long userId) {
		
		List<UserGroup> userGroups = repository.findAllByUserId(userId);
		
		List<Long> groupIds = userGroups
				.stream()
				.map(ug -> ug.getId().getGroupId())
				.collect(Collectors.toList());
		
		return groupRepository.findAllById(groupIds)
				.stream()
				.map(group -> groupAssembler.buildDtoWithLinksFromEntity(group))
				.collect(Collectors.toCollection(ArrayList::new));
	
	}

	
	@Override
	public Collection<EntityModel<UserDto>> findUsersByGroupId(Long groupId) {
		
		List<UserGroup> userGroups = repository.findAllByGroupId(groupId);
		
		List<Long> userIds = userGroups
				.stream()
				.map(ug -> ug.getId().getUserId())
				.collect(Collectors.toList());
		
		return userRepository.findAllById(userIds)
				.stream()
				.map(user -> userAssembler.buildDtoWithLinksFromEntity(user))
				.collect(Collectors.toCollection(ArrayList::new));
		
	}

	@Override
	public Collection<EntityModel<UserGroupDto>> findUserGroupsByUserId(Long userId) {
		
		return assembler.buildDtosWithLinksFromEntities(
				repository.findAllByUserId(userId));
	}

	@Override
	public Collection<EntityModel<UserGroupDto>> findUserGroupsByGroupId(Long groupId) {
		
		return assembler.buildDtosWithLinksFromEntities(
				repository.findAllByGroupId(groupId));
	}
	
}
 