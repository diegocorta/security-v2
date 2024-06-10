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
@Relation(collectionRelation = "userGroupRole")
public class UserGroupRoleDto 
		extends AbstractCommonDto {

	private static final long serialVersionUID = 1292584996827116732L;

	@NotNull
	private Long userId;
	
	@NotNull
	private Long groupId;
	
	@NotNull
	private Long roleId;
	
	@NotNull
	private Boolean isGranted; 
    
}
