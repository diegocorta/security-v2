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
import lombok.RequiredArgsConstructor;
import pm.security.v2.api.entity.embedded.key.RolePermissionId;
import pm.security.v2.api.service.IRolePermissionService;
import pm.security.v2.common.dto.RolePermissionDto;
import pm.security.v2.common.dto.min.PermissionMinDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/roles/{role-id}/permissions")
public class RolePermissionController {
	
	private final IRolePermissionService service;
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/permissions")
	@Operation(summary = "Returns all existing permission of a role",
			description = "Retrieves every permission for the specified role identifier in the path",
			responses = {@ApiResponse(responseCode = "200", description = "Permissions retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<PermissionMinDto>>> getPermissionsOfRole(
			@Parameter(description = "The identifier of the role") @PathVariable(name = "role-id") Long roleId) {
		
		var response = CollectionModel.of(service.findPermissionMinByRoleId(roleId));
		
		response.add(
				linkTo( methodOn(RolePermissionController.class).getPermissionsOfRole(roleId) )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping
	@Operation(summary = "Returns all existing permission of a role",
			description = "Retrieves every permission for the specified role identifier in the path",
			responses = {@ApiResponse(responseCode = "200", description = "Permissions retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<RolePermissionDto>>> getRolePermissions(
			@Parameter(description = "The identifier of the role") @PathVariable(name = "role-id") Long roleId) {
		
		var response = CollectionModel.of(service.findRolePermissionByRole(roleId));
		
		response.add(
				linkTo( methodOn(RolePermissionController.class).getRolePermissions(roleId) )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping
	@Operation(summary = "Create role-permissions",
			description = "Create a role-permissions with the information provided",
			responses = {@ApiResponse(responseCode = "201", description = "Role-permissions created successfully")})
	public ResponseEntity<CollectionModel<EntityModel<RolePermissionDto>>> createRolePermission(
			@Parameter(description = "The identifier of the role") @PathVariable(name = "role-id") Long roleId,
			@Valid @RequestBody List<@Valid RolePermissionDto> rolePermissionDto) {
		
		Set<Long> roleIds = rolePermissionDto
				.stream()
				.map(RolePermissionDto::getRoleId).collect(Collectors.toSet());
		
		Assert.isTrue(roleIds.size() == 1 && roleIds.contains(roleId), "error adding role to user group. ERROR MESSAGE TODO");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(CollectionModel.of(
				service.saveAll(rolePermissionDto)));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@DeleteMapping
	@Operation(summary = "Removes the received role-permssions",
			description = "Removes all the role-permissions that matches the information provided",
			responses = {@ApiResponse(responseCode = "204", description = "Role-permissions retrieved successfully")})
	public ResponseEntity<Void> deleteRolePermission(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "role-id") Long roleId,
			@Valid @RequestBody List<@Valid RolePermissionDto> rolePermissionDto) {
	
		Set<Long> roleIds = rolePermissionDto
				.stream()
				.map(RolePermissionDto::getRoleId).collect(Collectors.toSet());
		
		Assert.isTrue(roleIds.size() == 1 && roleIds.contains(roleId), "error adding role to user group. ERROR MESSAGE TODO");
		
		service.deleteByIds(rolePermissionDto
				.stream()
				.map(rp -> new RolePermissionId(rp.getPermissionId(), rp.getRoleId()))
				.collect(Collectors.toList()));
		
		return ResponseEntity.noContent().build();
	}
	
	
}
