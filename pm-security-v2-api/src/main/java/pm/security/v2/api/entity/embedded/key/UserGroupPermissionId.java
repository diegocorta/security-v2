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
public class UserGroupPermissionId implements Serializable {
	
	
	private static final long serialVersionUID = -4679099896832435925L;


	@NotNull
	private Long userId;
    
	
    @NotNull
    private Long groupId;
    
    
    @NotNull
    private Long permissionId;
}