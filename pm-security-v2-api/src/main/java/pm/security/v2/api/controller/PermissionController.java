package pm.security.v2.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import pm.security.v2.api.service.IPermissionService;
import pm.security.v2.common.dto.PermissionDto;
import pm.security.v2.common.dto.min.PermissionMinDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/permissions")
public class PermissionController {
	
	private final IPermissionService service;

	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping
	@Operation(summary = "Returns all existing permissions",
			description = "Retrieves every permission available",
			responses = {@ApiResponse(responseCode = "200", description = "Permissions retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<PermissionDto>>> getPermissions() {
		
		var response = CollectionModel.of(service.findAll());
		
		response.add(
				linkTo( methodOn(PermissionController.class).getPermissions() )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/minified")
	@Operation(summary = "Returns all existing permissions minified",
			description = "Retrieves every permission available with minimum information",
			responses = {@ApiResponse(responseCode = "200", description = "Permissions retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<PermissionMinDto>>> getPermissionsMinified() {
		
		var response = CollectionModel.of(service.findAllMinified());
		
		response.add(
				linkTo( methodOn(PermissionController.class).getPermissionsMinified() )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Returns one permission",
			description = "Returns the specific permission that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "Permission retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "Permission not found")})
	public ResponseEntity<EntityModel<PermissionDto>> getPermission(
			@Parameter(description = "The identifier of the permission") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findById(id));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{id}/minified")
	@Operation(summary = "Returns one permission minified",
			description = "Returns the specific permission, with minimum information, that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "Permission retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "Permission not found")})
	public ResponseEntity<EntityModel<PermissionMinDto>> getPermissionMinified(
			@Parameter(description = "The identifier of the permission") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findByIdMinified(id));
	}
	
}
