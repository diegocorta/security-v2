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
import pm.security.v2.api.entity.embedded.key.GroupRoleId;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pm_group_role")
public class GroupRole 
		extends AbstractCommonEntity<GroupRoleId>
		implements Serializable {

	
	private static final long serialVersionUID = -239147447603197314L;

	public static final String ROLE = "role";
    public static final String GROUP = "group";
	
	/**
     * Default object description for message composition
     */
    public static final String DEFAULT_DESCRIPTION = "group_role";
    
    
    @EmbeddedId
    private GroupRoleId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id",
			foreignKey = @ForeignKey(name = "fk_group_group_role"),
			nullable = false,
			updatable = false)
    @MapsId("groupId")
    @EqualsAndHashCode.Exclude
    private Group group;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",
			foreignKey = @ForeignKey(name = "fk_role_group_role"),
			nullable = false,
			updatable = false)
    @MapsId("roleId")
    @EqualsAndHashCode.Exclude
    private Role role;
}
