package pm.security.v2.common.dto.min;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(collectionRelation = "groups")
public class GroupMinDto {
	
	private Long id;
	
	@NotBlank(message = "name must not be null, nor empty")
    @Size(min = 3, max = 20, message = "name must contain between 3 and 20 characters")
    private String name;
    
    
	@Size(min = 0, max = 1000, message = "description must contain a max of 1000 characters")
    private String description;
    
}
