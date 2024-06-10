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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import pm.security.v2.api.entity.embedded.key.UserGroupId;
import pm.security.v2.api.service.IUserGroupService;
import pm.security.v2.common.dto.GroupDto;
import pm.security.v2.common.dto.UserGroupDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/{user-id}/groups")
public class UserGroupController {
	
	private final IUserGroupService service;
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("groups")
	@Operation(summary = "Returns all existing group of a user",
			description = "Retrieves every group for the specified user identifier in the path",
			responses = {@ApiResponse(responseCode = "200", description = "Groups retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<GroupDto>>> getGroupsOfUser(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId) {
		
		var response = CollectionModel.of(service.findGroupsByUserId(userId));
		
		response.add(
				linkTo( methodOn(UserGroupController.class).getGroupsOfUser(userId) )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping
	@Operation(summary = "Returns all existing user-groups",
			description = "Retrieves every user-groups for the specified user identifier in the path",
			responses = {@ApiResponse(responseCode = "200", description = "Groups retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserGroupDto>>> getUserGroups(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId) {
		
		var response = CollectionModel.of(service.findUserGroupsByUserId(userId));
		
		response.add(
				linkTo( methodOn(UserGroupController.class).getUserGroups(userId) )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping
	@Operation(summary = "Create group-users",
			description = "Create group-users with the information provided",
			responses = {@ApiResponse(responseCode = "201", description = "Group-users created successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserGroupDto>>> createGroupUsers(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@RequestBody @NotNull List<@Valid UserGroupDto> userGroupDto) {
		
		Set<Long> userIds = userGroupDto
				.stream()
				.map(UserGroupDto::getUserId).collect(Collectors.toSet());
		
		Assert.isTrue(userIds.size() == 1 && userIds.contains(userId), "error adding group to user. ERROR MESSAGE TODO");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				CollectionModel.of(service.saveAll(userGroupDto)));
	
	}
	
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@DeleteMapping
	@Operation(summary = "Removes the received Group-user",
			description = "Removes all the group-user that matches the information provided",
			responses = {@ApiResponse(responseCode = "204", description = "Group-user removed successfully")})
	public ResponseEntity<Void> deleteGroupUsers(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long userId,
			@RequestBody @NotNull List<@Valid UserGroupDto> userGroupDto) {
		
		Set<Long> userIds = userGroupDto
				.stream()
				.map(UserGroupDto::getUserId).collect(Collectors.toSet());
		
		Assert.isTrue(userIds.size() == 1 && userIds.contains(userId), "error deleting group to user. ERROR MESSAGE TODO");
		
		service.deleteByIds(userGroupDto
				.stream()
				.map(ug -> new UserGroupId(ug.getUserId(), ug.getGroupId()))
				.collect(Collectors.toList()));
		
		return ResponseEntity.noContent().build();
	}
	
}
