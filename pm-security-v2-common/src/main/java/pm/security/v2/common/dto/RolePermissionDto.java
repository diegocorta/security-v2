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
@Relation(collectionRelation = "rolePermission")
public class RolePermissionDto 
		extends AbstractCommonDto {

	private static final long serialVersionUID = -3887564623074241499L;

	@NotNull
	private Long RoleId;
	
	@NotNull
	private Long PermissionId;
    
}
