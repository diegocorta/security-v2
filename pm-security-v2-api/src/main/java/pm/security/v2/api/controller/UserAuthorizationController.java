package pm.security.v2.api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
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
import pm.security.v2.api.service.IUserAuthenticationService;
import pm.security.v2.api.service.IUserService;
import pm.security.v2.common.dto.JwtResponseDto;
import pm.security.v2.common.dto.UserEffectiveAuthorizationsDto;
import pm.security.v2.common.dto.UserLoginDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/users")
public class UserAuthorizationController {
	
	private final IUserService service;
	private final IUserAuthenticationService userPasswordService;
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@GetMapping("/{username}/effective-authorizations")
	@Operation(summary = "Returns the authorization of the user",
			description = "Retrieves the authorization of the user by its username. The authorizations will contain"
					+ "all the roles the user has, and a separated list with the specific permissions granted or revoked",
			responses = {
					@ApiResponse(responseCode = "200", description = "Authorizations retrieved successfully"),
					@ApiResponse(responseCode = "404", description = "User not found")})
	public ResponseEntity<UserEffectiveAuthorizationsDto> getUserAuthoritations(
			@Parameter(description = "The username of the user") @PathVariable String username) {
		
		return ResponseEntity.ok(service.findUserEffectivePermissions(username));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 * @throws Exception 
	 */
	@PostMapping("/login")
	@Operation(summary = "Returns a token to authenticate the user",
			description = "Retrieves a valid token to authenticate a user. The token should be used to perform any request",
			responses = {
					@ApiResponse(responseCode = "200", description = "Token retrieved successfully")})
	public ResponseEntity<JwtResponseDto> getAuthentication(
			@RequestBody @NotNull @Valid UserLoginDto dto) throws Exception {
				
		return ResponseEntity.ok(userPasswordService.login(dto));
	}
	
	/**
	 * Returns all existing users
	 * 
	 * @return all users
	 */
	@PostMapping("/refresh")
	@Operation(summary = "Returns a token to authenticate the user",
			description = "Retrieves a valid token to authenticate a user. The token should be used to perform any request",
			responses = {
					@ApiResponse(responseCode = "200", description = "Token retrieved successfully")})
	public ResponseEntity<JwtResponseDto> getRefreshToken(
			@RequestBody @Valid UUID refreshToken) {
		
		return ResponseEntity.ok(userPasswordService.refresh(refreshToken));
	}
	
}
