package pm.security.v2.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import pm.security.v2.api.service.IGroupService;
import pm.security.v2.common.dto.GroupDto;
import pm.security.v2.common.dto.min.GroupMinDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/groups")
public class GroupController {
	
	private final IGroupService service;

	/**
	 * Returns all existing groups
	 * 
	 * @return all gruops
	 */
	@GetMapping
	@Operation(summary = "Returns all existing groups",
			description = "Retrieves every group available",
			responses = {@ApiResponse(responseCode = "200", description = "Groups retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<GroupDto>>> getGroups() {
		
		var response = CollectionModel.of(service.findAll());
		
		response.add(
				linkTo( methodOn(GroupController.class).getGroups() )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/minified")
	@Operation(summary = "Returns all existing groups minified",
			description = "Retrieves every group available with minimum information",
			responses = {@ApiResponse(responseCode = "200", description = "Groups retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<GroupMinDto>>> getGroupsMinified() {
		
		var response = CollectionModel.of(service.findAllMinified());
		
		response.add(
				linkTo( methodOn(GroupController.class).getGroupsMinified() )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing group
	 * 
	 * @return all group
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Returns one group",
			description = "Returns the specific group that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "group retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "Group not found")})
	public ResponseEntity<EntityModel<GroupDto>> getGroup(
			@Parameter(description = "The identifier of the group") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findById(id));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{id}/minified")
	@Operation(summary = "Returns one group minified",
			description = "Returns the specific group, with minimum information, that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "persons retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "Group not found")})
	public ResponseEntity<EntityModel<GroupMinDto>> getGroupMinified(
			@Parameter(description = "The identifier of the group") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findByIdMinified(id));
	}

	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping
	@Operation(summary = "Create a group",
			description = "Create a user with the information provided",
			responses = {@ApiResponse(responseCode = "201", description = "User created successfully")})
	public ResponseEntity<EntityModel<GroupDto>> createGroups(
			@RequestBody @Valid GroupDto dto) {
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(service.save(dto));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Update an existing group",
			description = "Update a user with the information provided",
			responses = {@ApiResponse(responseCode = "200", description = "Person update successfully")})
	public ResponseEntity<EntityModel<GroupDto>> updateGroups(
			@RequestBody @Valid GroupDto dto) {
		
		return ResponseEntity.ok(service.update(dto));
	}
	
}
