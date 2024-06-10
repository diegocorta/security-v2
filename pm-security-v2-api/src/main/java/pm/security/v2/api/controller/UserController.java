package pm.security.v2.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.lang.Assert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pm.security.v2.api.service.IUserService;
import pm.security.v2.common.dto.UserDto;
import pm.security.v2.common.dto.min.RoleMinDto;
import pm.security.v2.common.dto.min.UserMinDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/users")
public class UserController {
	
	private final IUserService service;

	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping
	@Operation(summary = "Returns all existing users",
			description = "Retrieves every user available",
			responses = {@ApiResponse(responseCode = "200", description = "Users retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getUsers() {
		
		var response = CollectionModel.of(service.findAll());
		
		response.add(
				linkTo( methodOn(UserController.class).getUsers() )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/minified")
	@Operation(summary = "Returns all existing users minified",
			description = "Retrieves every user available with minimum information",
			responses = {@ApiResponse(responseCode = "200", description = "Users retrieved successfully")})
	public ResponseEntity<CollectionModel<EntityModel<UserMinDto>>> getUsersMinified() {
		
		var response = CollectionModel.of(service.findAllMinified());
		
		response.add(
				linkTo( methodOn(UserController.class).getUsersMinified() )
						.withSelfRel() );
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Returns one user",
			description = "Returns the specific user that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "User retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "User not found")})
	public ResponseEntity<EntityModel<UserDto>> getUser(
			@Parameter(description = "The identifier of the user") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findById(id));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{id}/minified")
	@Operation(summary = "Returns one user minified",
			description = "Returns the specific user, with minimum information, that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "User retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "User not found")})
	public ResponseEntity<EntityModel<UserMinDto>> getUserMinified(
			@Parameter(description = "The identifier of the user") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findByIdMinified(id));
	}

	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping
	@Operation(summary = "Create a user",
			description = "Create a user with the information provided",
			responses = {@ApiResponse(responseCode = "201", description = "User created successfully")})
	public ResponseEntity<EntityModel<UserDto>> createUsers(
			@RequestBody @Valid UserDto dto) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Update an existint user",
			description = "Update a user with the information provided",
			responses = {@ApiResponse(responseCode = "200", description = "User update successfully")})
	public ResponseEntity<EntityModel<UserDto>> updateUsers(
			@Parameter(description = "The identifier of the user") @PathVariable Long id,
			@RequestBody @Valid UserDto dto) {
		
		Assert.isTrue(dto.getId().equals(id), "body identifier must be the same as the path identifier");
		
		return ResponseEntity.ok(service.update(dto));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Update an existint user",
			description = "Update a user with the information provided",
			responses = {@ApiResponse(responseCode = "200", description = "User update successfully")})
	public ResponseEntity<Void> deleteUsers(
			@Parameter(description = "The identifier of the user") @PathVariable Long id) {
		
		service.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
	
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{id}/effective-roles")
	@Operation(summary = "Returns one user",
			description = "Returns the specific user that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "200", description = "User retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "User not found")})
	public ResponseEntity<Collection<EntityModel<RoleMinDto>>> getRolesOfUser(
			@Parameter(description = "The identifier of the user") @PathVariable Long id) {
		
		return ResponseEntity.ok(service.findUserEffectiveRoles(id));
	}
	
}
