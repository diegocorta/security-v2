package pm.security.v2.common.dto;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.common.dto.AbstractCommonDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(collectionRelation = "users")
public class UserDto 
		extends AbstractCommonDto {

	private static final long serialVersionUID = 8886469780894478454L;
	
	public static final String MINIMIZED_REL = "minimized";
	public static final String GROUP_REL = "groups";
	public static final String ROLES_REL = "roles";
//	public static final String GROUP_MIN_REL = "groups-minified";

	
	private Long id;
	
	
	@NotBlank(message = "username must not be null, nor empty")
    @Size(min = 3, max = 20, message = "username must contain between 3 and 20 characters")
    private String username;
    
	
    @NotNull
    private Boolean allowLogin;
    
    @NotNull
    @Email(message = "Email is not valid")
    private String email;
    
    private boolean hasPassword;
    
}
