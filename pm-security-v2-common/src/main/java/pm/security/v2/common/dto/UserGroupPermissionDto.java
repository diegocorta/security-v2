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
@Relation(collectionRelation = "userGroupPermission")
public class UserGroupPermissionDto 
		extends AbstractCommonDto {

	private static final long serialVersionUID = 5514526062808988914L;

	@NotNull
	private Long userId;
	
	@NotNull
	private Long groupId;
	
	@NotNull
	private Long permissionId;
	
	@NotNull
	private Boolean isGranted; 
    
}
