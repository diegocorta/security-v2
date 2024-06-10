package pm.security.v2.common.dto;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.common.dto.AbstractCommonDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(collectionRelation = "groupRole")
public class GroupRoleDto 
		extends AbstractCommonDto {

	private static final long serialVersionUID = 4926676066802611572L;

	@NotNull
	private Long groupId;
	
	@NotNull
	private Long roleId;
    
}
