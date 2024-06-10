package pm.security.v2.api.entity;

import java.io.Serializable;

import es.common.entity.AbstractCommonEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pm.security.v2.api.entity.embedded.key.RolePermissionId;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pm_role_permission")
public class RolePermission  
		extends AbstractCommonEntity<RolePermissionId>
		implements Serializable {

	
	private static final long serialVersionUID = -1893463559166210822L;

	public static final String ROLE = "role";
    public static final String PERMISSION = "permission";
    
	/**
     * Default object description for message composition
     */
    public static final String DEFAULT_DESCRIPTION = "role_permission";
    
    
    @EmbeddedId
    private RolePermissionId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",
			foreignKey = @ForeignKey(name = "fk_role_role_permission"),
			nullable = false,
			updatable = false)
    @MapsId("roleId")
    @EqualsAndHashCode.Exclude
    private Role role;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id",
			foreignKey = @ForeignKey(name = "fk_permission_role_permission"),
			nullable = false,
			updatable = false)
    @MapsId("permissionId")
    @EqualsAndHashCode.Exclude
    private Permission permission;
}
