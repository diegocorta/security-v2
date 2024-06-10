package pm.security.v2.api.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import es.common.entity.AbstractCommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pm_role")
public class Role 
		extends AbstractCommonEntity<Long>
		implements Serializable {

	
	private static final long serialVersionUID = -1728040790412222364L;


	/**
     * Default object description for message composition
     */
    public static final String DEFAULT_DESCRIPTION = "role";
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    
    @Column(name = "code",
            length = 10,
            nullable = false,
            updatable = false,
            unique = true)
    @NotBlank(message = "code must not be null, nor empty")
    @Size(min = 2, max = 10, message = "code must contain between 2 and 10 characters")
    private String code;
    
    
    @Column(name = "name",
            length = 30,
            nullable = false,
            updatable = true,
            unique = true)
    @NotBlank(message = "name must not be null, nor empty")
    @Size(min = 3, max = 40, message = "name must contain between 3 and 20 characters")
    private String name;
    
    
    @Column(name = "description",
            length = 1000,
            nullable = false,
            updatable = true)
    private String description;
	
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perent_role",
    		foreignKey = @ForeignKey(name = "fk_role_role"),
    		nullable = true,
    		updatable = true)
    @EqualsAndHashCode.Exclude
    private Role parentRole;
    
    
    @EqualsAndHashCode.Exclude
	@OneToMany(mappedBy="parentRole", fetch = FetchType.LAZY)
	private Set<Role> sonRoles = new HashSet<>();
    
    
    @EqualsAndHashCode.Exclude
	@OneToMany(mappedBy="role", fetch = FetchType.LAZY)
	private Set<RolePermission> rolePermission = new HashSet<>();
}
