package pm.security.v2.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import pm.security.v2.api.service.IUserAuthenticationService;
import pm.security.v2.common.dto.ChangeMyPasswordDto;
import pm.security.v2.common.dto.PasswordDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/users/{user-id}/passwords")
public class PasswordController {
	
	private final IUserAuthenticationService service;
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping
	@Operation(summary = "Returns one permission minified",
			description = "Returns the specific permission, with minimum information, that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "201", description = "Password created successfully")})
	public ResponseEntity<PasswordDto> createPassword(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long iserId,
			@RequestBody @Valid @NotNull PasswordDto dto) {
		
		return ResponseEntity.ok(service.createPassword(dto));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PreAuthorize("#dto.username == authentication.principal.username")
	@PostMapping("/my-password")
	@Operation(summary = "Returns one permission minified",
			description = "Returns the specific permission, with minimum information, that matches the identifier",
			responses = {
					@ApiResponse(responseCode = "201", description = "Password created successfully")})
	public ResponseEntity<PasswordDto> createMyPassword(
			@Parameter(description = "The identifier of the user") @PathVariable(name = "user-id") Long iserId,
			@RequestBody @Valid @NotNull ChangeMyPasswordDto dto) {
		
		return ResponseEntity.ok().build();
	}
	
}
