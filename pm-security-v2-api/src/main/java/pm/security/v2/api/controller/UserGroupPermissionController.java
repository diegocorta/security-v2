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
import pm.security.v2.api.entity.embedded.key.UserGroupPermissionId;
import pm.security.v2.api.service.IUserGroupPermissionService;
import pm.security.v2.common.dto.UserGroupPermissionDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/{user-id}/groups/{group-id}/permissions")
public class UserGroupPermissionController {
	
	private final IUserGroupPermissionService service;
	
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping
	@Operation(summary = "Returns all existing user-group-permission of a user-group",
			description = "Retrieves every user-group-permission for the specified user-group identifiers in the path",
			responses = {@ApiResponse(responseCode = "200", description = "User-group-permissions retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserGroupPermissionDto>>> getUserGroupPermissions(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId) {
		
		var response = CollectionModel.of(
				service.findUserGroupPermissionByUserIdAndGroupId(userId, groupId));
		
		response.add(
				linkTo( methodOn(UserGroupPermissionController.class).getUserGroupPermissions(userId, groupId) )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping
	@Operation(summary = "Create user-group-permissions",
		description = "Create user-group-permissions with the information provided",
		responses = {@ApiResponse(responseCode = "201", description = "User-group-permissions created successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserGroupPermissionDto>>> createUserGroupPermissions(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId,
			@RequestBody @NotNull List<@Valid UserGroupPermissionDto> userGroupPermissionDto) {
		
		Set<Long> groupIds = userGroupPermissionDto
				.stream()
				.map(UserGroupPermissionDto::getGroupId).collect(Collectors.toSet());
		
		Set<Long> userIds = userGroupPermissionDto
				.stream()
				.map(UserGroupPermissionDto::getGroupId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding role to user group. ERROR MESSAGE TODO");
		Assert.isTrue(userIds.size() == 1 && userIds.contains(groupId), "error adding role to user group. ERROR MESSAGE TODO");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(CollectionModel.of(
				service.saveAll(userGroupPermissionDto)));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PutMapping
	@Operation(summary = "Updated user-group-permssions",
			description = "Updates user-group-permissions with the information provided",
			responses = {@ApiResponse(responseCode = "200", description = "User-group-permissions updated successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserGroupPermissionDto>>> updateUserGroupPermissions(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId,
			@RequestBody @NotNull List<@Valid UserGroupPermissionDto> userGroupPermissionDto) {
		
		Set<Long> groupIds = userGroupPermissionDto
				.stream()
				.map(UserGroupPermissionDto::getGroupId).collect(Collectors.toSet());
		
		Set<Long> userIds = userGroupPermissionDto
				.stream()
				.map(UserGroupPermissionDto::getGroupId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding role to user group. ERROR MESSAGE TODO");
		Assert.isTrue(userIds.size() == 1 && userIds.contains(groupId), "error adding role to user group. ERROR MESSAGE TODO");
		
		return ResponseEntity.ok(CollectionModel.of(
				service.updateAll(userGroupPermissionDto)));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@DeleteMapping
	@Operation(summary = "Removes the received user-group-permissions",
			description = "Removes all the user-group-permissions that matches the information provided",
			responses = {@ApiResponse(responseCode = "204", description = "User-group-permissions removed successfully")})
	public ResponseEntity<Void> deleteUserGroupPermissions(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId,
			@RequestBody @NotNull List<@Valid UserGroupPermissionDto> userGroupPermissionDto) {		
		
		Set<Long> groupIds = userGroupPermissionDto
				.stream()
				.map(UserGroupPermissionDto::getGroupId).collect(Collectors.toSet());
		
		Set<Long> userIds = userGroupPermissionDto
				.stream()
				.map(UserGroupPermissionDto::getUserId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding role to user group. ERROR MESSAGE TODO");
		Assert.isTrue(userIds.size() == 1 && userIds.contains(userId), "error adding role to user group. ERROR MESSAGE TODO");
		
		
		service.deleteByIds(userGroupPermissionDto
				.stream()
				.map(ugp -> new UserGroupPermissionId(ugp.getUserId(), ugp.getGroupId(), ugp.getPermissionId()))
				.collect(Collectors.toList()));
		
		return ResponseEntity.noContent().build();
	}
	
}
