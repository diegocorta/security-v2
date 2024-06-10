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
import pm.security.v2.common.dto.UserDto;
import pm.security.v2.common.dto.UserGroupDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/groups/{group-id}/users")
public class GroupUserController {
	
	private final IUserGroupService service;
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("users")
	@Operation(summary = "Returns all existing user of a group",
			description = "Retrieves every user for the specified group identifier in the path",
			responses = {@ApiResponse(responseCode = "200", description = "Users retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getUsersOfGroup(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "group-id") Long groupId) {
		
		var response = CollectionModel.of(service.findUsersByGroupId(groupId));
		
		response.add(
				linkTo( methodOn(GroupUserController.class).getUsersOfGroup(groupId) )
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
	public ResponseEntity<CollectionModel<EntityModel<UserGroupDto>>> getGroupUsers(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "group-id") Long groupId) {
		
		var response = CollectionModel.of(service.findUserGroupsByUserId(groupId));
		
		response.add(
				linkTo( methodOn(UserGroupController.class).getGroupsOfUser(groupId) )
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
	public ResponseEntity<CollectionModel<EntityModel<UserGroupDto>>> createUserGroups(
			@Parameter(description = "The identifier of the group") @PathVariable(name = "group-id") Long groupId,
			@RequestBody @NotNull List<@Valid UserGroupDto> userGroupDto) {
		
		Set<Long> groupIds = userGroupDto
				.stream()
				.map(UserGroupDto::getGroupId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding user to group. ERROR MESSAGE TODO");
		
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
			responses = {@ApiResponse(responseCode = "204", description = "Group-user retrieved successfully")})
	public ResponseEntity<Void> deleteUserGroups(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "group-id") Long groupId,
			@RequestBody @NotNull List<@Valid UserGroupDto> userGroupDto) {
		
		Set<Long> groupIds = userGroupDto
				.stream()
				.map(UserGroupDto::getGroupId).collect(Collectors.toSet());
		
		Assert.isTrue(groupIds.size() == 1 && groupIds.contains(groupId), "error adding user to group. ERROR MESSAGE TODO");
		
		service.deleteByIds(userGroupDto
				.stream()
				.map(ug -> new UserGroupId(ug.getUserId(), ug.getGroupId()))
				.collect(Collectors.toList()));
		
		return ResponseEntity.noContent().build();
	}
	
	
}
