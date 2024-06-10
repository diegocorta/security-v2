package pm.security.v2.api.service;

import java.util.UUID;

import pm.security.v2.common.dto.JwtResponseDto;
import pm.security.v2.common.dto.PasswordDto;
import pm.security.v2.common.dto.UserLoginDto;

public interface IUserAuthenticationService {

	/**
	 * Method that received the information of the user, tries to authenticate it,
	 * obtaining a valid token
	 * 
	 * @param dto
	 * @return
	 */
	public JwtResponseDto login(UserLoginDto dto);
	
	/**
	 * Method that received the information of the user, tries to authenticate it,
	 * obtaining a valid token
	 * 
	 * @param dto
	 * @return
	 */
	public JwtResponseDto refresh(UUID refreshToken);
	
	public PasswordDto createPassword(PasswordDto password);
}
