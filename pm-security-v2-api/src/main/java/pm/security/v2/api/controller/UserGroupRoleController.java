package pm.security.v2.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import pm.security.v2.api.entity.embedded.key.UserGroupRoleId;
import pm.security.v2.api.service.IUserGroupRoleService;
import pm.security.v2.common.dto.UserGroupRoleDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/{user-id}/groups/{group-id}/roles")
public class UserGroupRoleController {
	
	private final IUserGroupRoleService service;
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping
	@Operation(summary = "Returns all existing user-group-role of a user-group",
			description = "Retrieves every user-group-role for the specified user-group identifiers in the path",
			responses = {@ApiResponse(responseCode = "200", description = "User-group-role retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserGroupRoleDto>>> getUserGroupRoles(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId) {
		
		var response = CollectionModel.of(
				service.findUserGroupRoleByUserIdAndGroupId(userId, groupId));
		
		response.add(
				linkTo( methodOn(UserGroupRoleController.class).getUserGroupRoles(userId, groupId) )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);

	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping
	@Operation(summary = "Create user-group-roles",
			description = "Create user-group-roles with the information provided",
			responses = {@ApiResponse(responseCode = "201", description = "User-group-roles created successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserGroupRoleDto>>> createUserGroupRoles(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId,
			@RequestBody @NotNull List<@Valid UserGroupRoleDto> userGroupRoleDto) {
		
		Set<Long> groupIds = userGroupRoleDto
				.stream()
				.map(UserGroupRoleDto::getGroupId).collect(Collectors.toSet());
		
		Set<Long> userIds = userGroupRoleDto
				.stream()
				.map(UserGroupRoleDto::getUserId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding role to user group. ERROR MESSAGE TODO");
		Assert.isTrue(userIds.size() == 1 && userIds.contains(userId), "error adding role to user group. ERROR MESSAGE TODO");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(CollectionModel.of(
				service.saveAll(userGroupRoleDto)));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PutMapping
	@Operation(summary = "Updated user-group-roles",
			description = "Updates user-group-roles with the information provided",
			responses = {@ApiResponse(responseCode = "200", description = "User-group-roles updated successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserGroupRoleDto>>> updateUserGroupRoles(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId,
			@RequestBody @NotNull List<@Valid UserGroupRoleDto> userGroupRoleDto) {
		
		Set<Long> groupIds = userGroupRoleDto
				.stream()
				.map(UserGroupRoleDto::getGroupId).collect(Collectors.toSet());
		
		Set<Long> userIds = userGroupRoleDto
				.stream()
				.map(UserGroupRoleDto::getUserId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding role to user group. ERROR MESSAGE TODO");
		Assert.isTrue(userIds.size() == 1 && userIds.contains(userId), "error adding role to user group. ERROR MESSAGE TODO");
		
		return ResponseEntity.ok(CollectionModel.of(
				service.updateAll(userGroupRoleDto)));
	}
	
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@DeleteMapping
	@Operation(summary = "Removes the received user-group-roles",
			description = "Removes all the user-group-roles that matches the information provided",
			responses = {@ApiResponse(responseCode = "204", description = "User-group-roles removed successfully")})
	public ResponseEntity<Void> deleteUserGroupRoles(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId,
			@RequestBody @NotNull List<@Valid UserGroupRoleDto> userGroupRoleDto) {		
		
		Set<Long> groupIds = userGroupRoleDto
				.stream()
				.map(UserGroupRoleDto::getGroupId).collect(Collectors.toSet());
		
		Set<Long> userIds = userGroupRoleDto
				.stream()
				.map(UserGroupRoleDto::getUserId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding role to user group. ERROR MESSAGE TODO");
		Assert.isTrue(userIds.size() == 1 && userIds.contains(userId), "error adding role to user group. ERROR MESSAGE TODO");
		
		service.deleteByIds(userGroupRoleDto
				.stream()
				.map(ugp -> new UserGroupRoleId(ugp.getUserId(), ugp.getGroupId(), ugp.getRoleId()))
				.collect(Collectors.toList()));
		
		return ResponseEntity.noContent().build();
	}
	
}
