package pm.security.v2.common.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtResponseDto {

	private String token;
	private String type = "Bearer";
	private String refreshToken;
	
}
