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
@Relation(collectionRelation = "permissions")
public class PermissionDto 
		extends AbstractCommonDto {
    
	
	private static final long serialVersionUID = 2618214915925842427L;


	private Long id;
	
	
	@NotBlank(message = "code must not be null, nor empty")
    @Size(min = 2, max = 10, message = "code must contain between 2 and 10 characters")
    private String code;
    
    
    @NotBlank(message = "name must not be null, nor empty")
    @Size(min = 3, max = 30, message = "name must contain between 3 and 20 characters")
    private String name;
    
    
    @Size(min = 0, max = 1000, message = "description must contain a max of 1000 characters")
    private String description;
	
}
