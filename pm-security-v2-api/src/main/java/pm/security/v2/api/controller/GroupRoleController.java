package pm.security.v2.api.controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pm.security.v2.api.entity.embedded.key.GroupRoleId;
import pm.security.v2.api.service.IGroupRoleService;
import pm.security.v2.common.dto.GroupRoleDto;
import pm.security.v2.common.dto.min.RoleMinDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/groups/{group-id}/roles")
public class GroupRoleController {
	
	private final IGroupRoleService service;
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("roles")
	@Operation(summary = "Returns all existing group-role of a group",
			description = "Retrieves every group-role for the specified group identifier in the path",
			responses = {@ApiResponse(responseCode = "200", description = "Group-role retrieved successfully")})
	public ResponseEntity<Collection<EntityModel<RoleMinDto>>> getRolesOfGroup(
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId) {
		
		return ResponseEntity.ok(service.findRolesMinOfGroup(groupId));

	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping
	@Operation(summary = "Returns all existing group-role of a group",
			description = "Retrieves every group-role for the specified group identifier in the path",
			responses = {@ApiResponse(responseCode = "200", description = "Group-role retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<GroupRoleDto>>> getGroupRole(
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId) {
		
		var response = CollectionModel.of(service.findGroupRolesByGroup(groupId));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping
	@Operation(summary = "Create a Group-role",
			description = "Create a group-role with the information provided",
			responses = {@ApiResponse(responseCode = "201", description = "Group-role created successfully")})
	public ResponseEntity<CollectionModel<EntityModel<GroupRoleDto>>> createGroupRoles(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "group-id") Long groupId,
			@Valid @RequestBody List<@Valid GroupRoleDto> groupRoleDtos) {
		
		Set<Long> groupIds = groupRoleDtos
				.stream()
				.map(GroupRoleDto::getGroupId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding group to user. ERROR MESSAGE TODO");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(CollectionModel.of(
				service.saveAll(groupRoleDtos)));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@DeleteMapping
	@Operation(summary = "Removes the existing Group-role",
			description = "Removes all the group-role that matches the information provided",
			responses = {@ApiResponse(responseCode = "204", description = "Group-role removed successfully")})
	public ResponseEntity<Void> deleteGroupRoles(
			@Parameter(description = "the identifier of the user") @PathVariable(name = "group-id") Long groupId,
			@Valid @RequestBody List<@Valid GroupRoleDto> groupRoleDtos) {
		
		Set<Long> groupIds = groupRoleDtos
				.stream()
				.map(GroupRoleDto::getGroupId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding group to user. ERROR MESSAGE TODO");
		
		service.deleteByIds(groupRoleDtos
				.stream()
				.map(ug -> new GroupRoleId(ug.getGroupId(), ug.getRoleId()))
				.collect(Collectors.toList()));
				
		return ResponseEntity.noContent().build();
	}
	
	
}
