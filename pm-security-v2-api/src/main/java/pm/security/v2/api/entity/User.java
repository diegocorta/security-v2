package pm.security.v2.api.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import es.common.entity.AbstractCommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "pm_user")
public class User 
		extends AbstractCommonEntity<Long>
		implements Serializable {

	
	private static final long serialVersionUID = -1371469569730646301L;

	
	/**
     * Default object description for message composition
     */
    public static final String DEFAULT_DESCRIPTION = "user";
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    
    @Column(name = "username",
            length = 20,
            nullable = false,
            updatable = true,
            unique = true)
    @NotBlank(message = "username must not be null, nor empty")
    @Size(min = 3, max = 20, message = "username must contain between 3 and 20 characters")
    private String username;
    
    @Column(name = "email",
            length = 50,
            nullable = false,
            updatable = true,
            unique = true)
    @Email(message = "Email is not valid")
    private String email;

    
    @Column(name = "allow_login",
            length = 20,
            nullable = false,
            updatable = true)
    private Boolean allowLogin;
    
    
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
	private Set<UserGroup> userGroup = new HashSet<>();
	
	
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
	private Set<UserGroupRole> userGroupRole = new HashSet<>();
	
	
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
	private Set<UserGroupPermission> userGroupPermission = new HashSet<>();
	
	 
	@EqualsAndHashCode.Exclude
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private Password password;
	
}
