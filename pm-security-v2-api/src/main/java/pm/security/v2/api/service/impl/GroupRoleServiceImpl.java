package pm.security.v2.api.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
import pm.security.v2.api.assembler.GroupAssembler;
import pm.security.v2.api.assembler.GroupRoleAssembler;
import pm.security.v2.api.assembler.RoleAssembler;
import pm.security.v2.api.entity.Group;
import pm.security.v2.api.entity.GroupRole;
import pm.security.v2.api.entity.Role;
import pm.security.v2.api.entity.embedded.key.GroupRoleId;
import pm.security.v2.api.repository.IGroupRepository;
import pm.security.v2.api.repository.IGroupRoleRepository;
import pm.security.v2.api.repository.IRoleRepository;
import pm.security.v2.api.service.IGroupRoleService;
import pm.security.v2.common.dto.GroupRoleDto;
import pm.security.v2.common.dto.min.GroupMinDto;
import pm.security.v2.common.dto.min.RoleMinDto;

@Service
public class GroupRoleServiceImpl extends BasicService<IGroupRoleRepository, GroupRole, GroupRoleId, GroupRoleDto, GroupRoleAssembler>
		implements IGroupRoleService {
	
	private final IRoleRepository roleRepository;
	private final IGroupRepository groupRepository;
	
	private final RoleAssembler roleAssembler;
	private final GroupAssembler groupAssembler;
	
	public GroupRoleServiceImpl(IGroupRoleRepository repository,
			IRoleRepository roleRepository, IGroupRepository groupRepository) {
		
		super(GroupRole.class, repository, GroupRoleAssembler.getInstance());
		
		this.roleRepository = roleRepository;
		this.groupRepository = groupRepository;
		
		this.roleAssembler = RoleAssembler.getInstance();
		this.groupAssembler = GroupAssembler.getInstance();
	}

	@Override
	public Map<GroupRoleDto, JoinEntityMap> getRelatedEntities(Collection<GroupRoleDto> dtos) {
		
		ConcurrentHashMap<GroupRoleDto, JoinEntityMap> result = new ConcurrentHashMap<>();
		
		// Obtaining all the identifier of users and groups
		List<Long> roleIds = dtos.parallelStream()
				.map(GroupRoleDto::getRoleId)
				.distinct()
				.toList();
		
		List<Long> groupIds = dtos.parallelStream()
				.map(GroupRoleDto::getGroupId)
				.distinct()
				.toList();
		
		// Search every entity located
		Map<Long, Role> roleMap = roleRepository.findAllById(roleIds)
				.stream()
				.collect(Collectors.toMap(Role::getId, Function.identity()));
		
		Map<Long, Group> groupMap = groupRepository.findAllById(groupIds)
				.stream()
				.collect(Collectors.toMap(Group::getId, Function.identity()));
		
		
		dtos.parallelStream().forEach(dto -> {
				
			Role role = roleMap.get(dto.getRoleId());
			Group group = groupMap.get(dto.getGroupId());

			if (role == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(Role.DEFAULT_DESCRIPTION));
						
			if (group == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(Group.DEFAULT_DESCRIPTION));
			
			Collection<Pair<String, Object>> joinEntityList = List.of(
					Pair.of(GroupRole.GROUP, group),
					Pair.of(GroupRole.ROLE, role)
			);

			result.put(dto, JoinEntityMap.from(joinEntityList));
			
		});
		
		return result;
	}

	@Override
	public void basicDataValidation(Collection<GroupRoleDto> dtos) {
		
	}

	@Override
	public void createDataValidation(Collection<GroupRoleDto> dtos) {
		// TODO Auto-generated method stub
	}

	@Override
	public Collection<EntityModel<RoleMinDto>> findRolesMinOfGroup(Long groupId) {
		
		Set<Long> roleIds = repository.findAllByGroupId(groupId)
				.stream()
				.map(gr -> gr.getId().getRoleId())
				.collect(Collectors.toSet());
		
		Set<Role> roles= roleRepository.findAllById(roleIds)
				.stream()
				.collect(Collectors.toSet());
		
		roles.addAll(roleRepository.findAllAncestorsRecursive(roleIds)
				.stream()
				.collect(Collectors.toCollection(HashSet::new)));
		
		return roleAssembler.buildMinDtosWithLinksFromEntities(roles, RoleMinDto.class);
	}

	@Override
	public Collection<EntityModel<GroupRoleDto>> findGroupRolesByGroup(Long groupId) {
		
		return assembler.buildDtosWithLinksFromEntities(
				repository.findAllByGroupId(groupId));
		
	}

	@Override
	public Collection<EntityModel<GroupMinDto>> findGroupMinOfRole(Long roleId) {
				
		Set<Long> roleIds = roleRepository.findById(roleId)
				.stream()
				.map(Role::getId)
				.collect(Collectors.toSet());
		
		Set<Long> descendantRoleIds = roleRepository.findAllDescendantsRecursive(roleIds)
				.stream().map(Role::getId)
				.collect(Collectors.toSet());
		
		roleIds.addAll(descendantRoleIds);
		
		Set<Long> groupIds = repository.findAllByGroupIdIn(roleIds)
				.stream()
				.map(gr -> gr.getId().getGroupId())
				.collect(Collectors.toSet());
		
		return groupRepository.findAllById(groupIds)
				.stream()
				.map(role -> groupAssembler.buildMinDtoWithLinksFromEntity(role, GroupMinDto.class))
				.collect(Collectors.toCollection(ArrayList::new));

	}
	
}
 