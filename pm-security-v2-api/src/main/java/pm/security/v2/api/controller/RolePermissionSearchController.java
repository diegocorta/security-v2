package pm.security.v2.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.Set;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import pm.security.v2.api.service.IRolePermissionService;
import pm.security.v2.common.dto.RolePermissionDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/role/permissions")
public class RolePermissionSearchController {
	
	private final IRolePermissionService service;
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PreAuthorize("hasAuthority('OP')")
	@GetMapping
	@Operation(summary = "Returns all existing role-permissions",
			description = "Retrieves every role-permission available",
			responses = {@ApiResponse(responseCode = "200", description = "Role-permissions retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<RolePermissionDto>>> getRolePermission() {
		
		var response = CollectionModel.of(service.findAll());
		
		response.add(
				linkTo( methodOn(RolePermissionSearchController.class).getRolePermission() )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/effective")
	@Operation(summary = "Returns an effective map of role-permission",
			description = "Retrieves an effective map of role-permissions. The key is the role code and "
					+ "every role will contain a list of its permission codes",
			responses = {@ApiResponse(responseCode = "200", description = "Effective role-permissions retrieved successfully")})
	public ResponseEntity<HashMap<String, Set<String>>> getEfectiveRolePermission() {
		
		return ResponseEntity.ok(service.findRolePermissionEfective());
		
	}
		
}
