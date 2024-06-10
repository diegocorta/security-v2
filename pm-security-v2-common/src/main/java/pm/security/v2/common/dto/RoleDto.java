package pm.security.v2.common.dto;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.common.dto.AbstractCommonDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(collectionRelation = "roles")
public class RoleDto 
		extends AbstractCommonDto {

	private static final long serialVersionUID = -8792797958546961530L;
	
	public static final String MINIMIZED_REL = "minimized";
	public static final String USERS_REL = "users";
	public static final String GROUPS_REL = "groups";
	public static final String PARENT_REL = "parent";
	
	private Long id;
	
	
	@NotBlank(message = "code must not be null, nor empty")
    @Size(min = 2, max = 10, message = "code must contain between 2 and 10 characters")
    private String code;
    
	
    @NotBlank(message = "name must not be null, nor empty")
    @Size(min = 3, max = 40, message = "name must contain between 3 and 20 characters")
    private String name;
    
    
    @Size(min = 0, max = 1000, message = "description must contain a max of 1000 characters")
    private String description;
	
    
    private Long parentRoleId;
    
}
