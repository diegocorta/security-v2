package pm.security.v2.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import lombok.RequiredArgsConstructor;
import pm.security.v2.api.service.IRoleService;
import pm.security.v2.common.dto.RoleDto;
import pm.security.v2.common.dto.min.RoleMinDto;
import pm.security.v2.common.dto.min.UserMinDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/roles")
public class RoleController {
	
	private final IRoleService service;

	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping
	@Operation(summary = "Returns all existing roles",
			description = "Retrieves every role available",
			responses = {@ApiResponse(responseCode = "200", description = "Roles retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<RoleDto>>> getRoles() {
		
		var response = CollectionModel.of(service.findAll());
		
		response.add(
				linkTo( methodOn(RoleController.class).getRoles() )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/minified")
	@Operation(summary = "Returns all existing roles minified",
			description = "Retrieves every role available with minimum information",
			responses = {@ApiResponse(responseCode = "200", description = "Roles retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<RoleMinDto>>> getRolesMinified() {
		
		var response = CollectionModel.of(service.findAllMinified());
		
		response.add(
				linkTo( methodOn(RoleController.class).getRolesMinified() )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Returns one role",
			description = "Returns the specific role that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "Role not found")})
	public ResponseEntity<EntityModel<RoleDto>> getRole(
			@Parameter(description = "The identifier of the role") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findById(id));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{id}/minified")
	@Operation(summary = "Returns one user role",
			description = "Returns the specific role, with minimum information, that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "Role not found")})
	public ResponseEntity<EntityModel<RoleMinDto>> getRoleMinified(
			@Parameter(description = "The identifier of the role") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findByIdMinified(id));
	}

	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping
	@Operation(summary = "Create a role",
			description = "Create a role with the information provided",
			responses = {@ApiResponse(responseCode = "201", description = "Role created successfully")})
	public ResponseEntity<EntityModel<RoleDto>> createRoles(
			@RequestBody @Valid RoleDto dto) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Update an existint role",
			description = "Update a role with the information provided",
			responses = {@ApiResponse(responseCode = "200", description = "Role update successfully")})
	public ResponseEntity<EntityModel<RoleDto>> updateRoles(
			@RequestBody @Valid RoleDto dto) {
		
		return ResponseEntity.ok(service.update(dto));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{id}/effective-users")
	@Operation(summary = "Returns one user",
			description = "Returns the specific user that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "User retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "User not found")})
	public ResponseEntity<Collection<EntityModel<UserMinDto>>> getUsersOfRole(
			@Parameter(description = "The identifier of the user") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findUserOfRoles(id));
	}
	
}
