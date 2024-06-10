package pm.security.v2.api.entity;

import java.io.Serializable;

import es.common.entity.AbstractCommonEntity;
import jakarta.persistence.Column;
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
import pm.security.v2.api.entity.embedded.key.UserGroupRoleId;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pm_user_group_role")
public class UserGroupRole  
		extends AbstractCommonEntity<UserGroupRoleId>
		implements Serializable {

	
	private static final long serialVersionUID = -7079196126957283262L;

	public static final String USER = "user";
    public static final String GROUP = "group";
    public static final String ROLE = "role";
    
	/**
     * Default object description for message composition
     */
    public static final String DEFAULT_DESCRIPTION = "user_group_role";
    
    
    @EmbeddedId
    private UserGroupRoleId id;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
			foreignKey = @ForeignKey(name = "fk_user_user_group_role"),
			nullable = false,
			updatable = false)
    @MapsId("userId")
    @EqualsAndHashCode.Exclude
    private User user;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id",
			foreignKey = @ForeignKey(name = "fk_group_user_group_role"),
			nullable = false,
			updatable = false)
    @MapsId("groupId")
    @EqualsAndHashCode.Exclude
    private Group group;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",
			foreignKey = @ForeignKey(name = "fk_role_user_group_role"),
			nullable = false,
			updatable = false)
    @MapsId("roleId")
    @EqualsAndHashCode.Exclude
    private Role role;
    
    
    @Column(name = "is_granted",
            nullable = false,
            updatable = true)
    @EqualsAndHashCode.Exclude
    private Boolean isGranted;
    
}
