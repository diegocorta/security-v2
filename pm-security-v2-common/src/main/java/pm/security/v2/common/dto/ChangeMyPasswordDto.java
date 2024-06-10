package pm.security.v2.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChangeMyPasswordDto {
	
	@NotNull
	@NotBlank
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
    @NotBlank(message = "The password must exists")
    @Size(min = 10, max = 30, message = "The password must contain between 10 an 30 characters")
    private String password;
	
	
	@JsonProperty(access = Access.WRITE_ONLY)
    @NotBlank(message = "The password must exists")
    @Size(min = 10, max = 30, message = "The password must contain between 10 an 30 characters")
    private String newPassword;
	
}
