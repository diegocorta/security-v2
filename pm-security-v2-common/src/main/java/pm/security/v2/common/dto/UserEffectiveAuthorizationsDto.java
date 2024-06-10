package pm.security.v2.common.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEffectiveAuthorizationsDto {

	private Set<String> roles;
	
	private Set<String> grantedPermissions;
	
	private Set<String> revokedPermissions;
	
}
