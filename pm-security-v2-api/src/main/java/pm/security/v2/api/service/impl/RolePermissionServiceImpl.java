package pm.security.v2.api.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import pm.security.v2.api.assembler.PermissionAssembler;
import pm.security.v2.api.assembler.RolePermissionAssembler;
import pm.security.v2.api.entity.Permission;
import pm.security.v2.api.entity.Role;
import pm.security.v2.api.entity.RolePermission;
import pm.security.v2.api.entity.embedded.key.RolePermissionId;
import pm.security.v2.api.repository.IPermissionRepository;
import pm.security.v2.api.repository.IRolePermissionRepository;
import pm.security.v2.api.repository.IRoleRepository;
import pm.security.v2.api.service.IRolePermissionService;
import pm.security.v2.common.dto.RolePermissionDto;
import pm.security.v2.common.dto.min.PermissionMinDto;

@Service
public class RolePermissionServiceImpl extends BasicService<IRolePermissionRepository, RolePermission, RolePermissionId, RolePermissionDto, RolePermissionAssembler>
		implements IRolePermissionService {
	
	private final IPermissionRepository permissionRepository;
	private final IRoleRepository roleRepository;
	
	private final PermissionAssembler permissionAssembler;
	
	public RolePermissionServiceImpl(IRolePermissionRepository repository,
			IPermissionRepository permissionRepository,
			IRoleRepository roleRepository) {
		
		super(RolePermission.class, repository, RolePermissionAssembler.getInstance());
		
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;

		this.permissionAssembler = PermissionAssembler.getInstance();
	}

	@Override
	public Map<RolePermissionDto, JoinEntityMap> getRelatedEntities(Collection<RolePermissionDto> dtos) {
			
		ConcurrentHashMap<RolePermissionDto, JoinEntityMap> result = new ConcurrentHashMap<>();
		
		// Obtaining all the identifier of users and groups
		List<Long> roleIds = dtos.parallelStream()
				.map(RolePermissionDto::getRoleId)
				.distinct()
				.toList();
		
		List<Long> permissionIds = dtos.parallelStream()
				.map(RolePermissionDto::getPermissionId)
				.distinct()
				.toList();
		
		// Search every entity located
		Map<Long, Role> roleMap = roleRepository.findAllById(roleIds)
				.stream()
				.collect(Collectors.toMap(Role::getId, Function.identity()));
		
		Map<Long, Permission> permissionMap = permissionRepository.findAllById(permissionIds)
				.stream()
				.collect(Collectors.toMap(Permission::getId, Function.identity()));
		
		
		dtos.parallelStream().forEach(dto -> {
				
			Role role = roleMap.get(dto.getRoleId());
			Permission permission = permissionMap.get(dto.getPermissionId());

			if (role == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(Role.DEFAULT_DESCRIPTION));
						
			if (permission == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(Permission.DEFAULT_DESCRIPTION));
			
			Collection<Pair<String, Object>> joinEntityList = List.of(
					Pair.of(RolePermission.PERMISSION, permission),
					Pair.of(RolePermission.ROLE, role)
			);

			result.put(dto, JoinEntityMap.from(joinEntityList));
			
		});
		
		return result;
	}

	@Override
	public void basicDataValidation(Collection<RolePermissionDto> dtos) {
		
	}

	@Override
	public void createDataValidation(Collection<RolePermissionDto> dtos) {
		// TODO Auto-generated method stub
	}

	@Override
	public Collection<EntityModel<PermissionMinDto>> findPermissionMinByRoleId(Long roleId) {
		
		Set<Long> roleIds = getAllAncestorRoles(roleId)
				.stream()
				.map(Role::getId)
				.collect(Collectors.toSet());
		
		roleIds.add(roleId);
		
		Set<Long> permissionIds = repository.findAllByRoleIdIn(roleIds)
				.stream()
				.map(rp -> rp.getId().getPermissionId())
				.collect(Collectors.toSet());
	
		return permissionRepository.findAllById(permissionIds)
				.stream()
				.map(permission -> permissionAssembler.buildMinDtoWithLinksFromEntity(permission, PermissionMinDto.class))
				.collect(Collectors.toCollection(ArrayList::new));
		
	}

	@Override
	public Collection<EntityModel<PermissionMinDto>> findPermissionMinByRoleIds(Collection<Long> roleIds) {
		
		Set<Long> effectiveRoleIds = new HashSet<>();
		
		effectiveRoleIds.addAll(roleIds);
		
		var rolesMap = roleRepository.findAll()
				.stream()
				.collect(Collectors.toMap(Role::getId, Function.identity()));
				
		for (Long roleId : roleIds) {
			
			var role = rolesMap.get(roleId);
			
			if (role == null)
				throw new EntityNotFoundException(
						MessageUtils.entityNotFoundExceptionMessage(Role.DEFAULT_DESCRIPTION));
			
			effectiveRoleIds.addAll(getAllAncestorRoles(role, rolesMap)
					.stream()
					.map(Role::getId)
					.collect(Collectors.toSet()));
		}
		
		Set<Long> permissionIds = repository.findAllByRoleIdIn(effectiveRoleIds)
				.stream()
				.map(rp -> rp.getId().getPermissionId())
				.collect(Collectors.toSet());
		
		return permissionRepository.findAllById(permissionIds)
				.stream()
				.map(permission -> permissionAssembler.buildMinDtoWithLinksFromEntity(permission, PermissionMinDto.class))
				.collect(Collectors.toCollection(ArrayList::new));
		
	}

	@Override
	public HashMap<String, Set<String>> findRolePermissionEfective() {
		
		// Obtain the role-permission, roles and permissions
		List<RolePermission> rolePermissions = repository.findAll();
		HashMap<String, Set<String>> result = new HashMap<String, Set<String>>();
		
		var rolesMap = roleRepository.findAll()
				.stream()
				.collect(Collectors.toMap(Role::getId, Function.identity()));
		
		var permissionsMap = permissionRepository.findAll()
				.stream()
				.collect(Collectors.toMap(Permission::getId, Function.identity()));
			
		// Iterate each role searching its ancestors. The permissions of the current role
		// will be all the permissions any of the ancestor roles has
		for (Role role : rolesMap.values()) {
			
			// Generate a list with all the identifiers of the ancestors
			Set<Long> roleIds = new HashSet<Long>();
			
			roleIds.add(role.getId());
			
			roleIds.addAll(getAllAncestorRoles(role, rolesMap)
					.stream()
					.map(Role::getId)
					.collect(Collectors.toSet()));

			
			// Obtain all the permission codes of all the ancestor obtained
			Set<String> codes = rolePermissions
					.stream()
					.filter(rolePermission -> roleIds.contains(rolePermission.getId().getRoleId()))
					.map(rolePermission -> rolePermission.getId().getPermissionId())
					.distinct()
					.map(permissionId -> permissionsMap.get(permissionId).getCode())
					.collect(Collectors.toSet());
			
			// add all the permission codes to the current role
			result.put(role.getCode(), codes);
		}
		
		return result;
	}
	
	@Override
	public Collection<EntityModel<RolePermissionDto>> findRolePermissionByRole(Long roleId) {
		
		return assembler.buildDtosWithLinksFromEntities(
				repository.findAllByRoleId(roleId));
	}
	
	private Set<Role> getAllAncestorRoles(Role role, Map<Long, Role> rolesMap) {
		
		Set<Role> ancestorSet = new HashSet<>();
		Role fatherRole = role;
		
		do {
			
			fatherRole = fatherRole.getParentRole();
			
			if (fatherRole != null) {
				// Solve the "proxy" with the data we already have
				fatherRole = rolesMap.get(fatherRole.getId());
				ancestorSet.add(fatherRole);
			}
			
		} while(fatherRole != null);
		
		return ancestorSet;
		
	}
	
	private Set<Role> getAllAncestorRoles(Long roleId) {
		
		var role = roleRepository.findById(roleId).orElseThrow(() 
				-> new EntityNotFoundException(
						MessageUtils.entityNotFoundExceptionMessage(Role.DEFAULT_DESCRIPTION)));
	
		var rolesMap = roleRepository
				.findAll()
				.stream()
				.collect(Collectors.toMap(Role::getId, Function.identity()));
		
		return getAllAncestorRoles(role, rolesMap);
		
	}
	
}
 