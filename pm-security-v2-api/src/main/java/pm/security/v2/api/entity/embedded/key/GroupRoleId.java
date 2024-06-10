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
public class GroupRoleId implements Serializable {

	
	private static final long serialVersionUID = -8648412417968231045L;
	

	@NotNull
    private Long groupId;
    
    
    @NotNull
    private Long roleId;
    

}