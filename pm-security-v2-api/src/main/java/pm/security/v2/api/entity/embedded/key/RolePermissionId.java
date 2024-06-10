package pm.security.v2.api.entity.embedded.key;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RolePermissionId implements Serializable {

	
	private static final long serialVersionUID = -2175353024128221269L;

	
	@NotNull
    private Long permissionId;
    
    
    @NotNull
    private Long roleId;
	    
}