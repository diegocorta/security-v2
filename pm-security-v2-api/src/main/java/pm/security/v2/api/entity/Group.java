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
@Table(name = "pm_group")
public class Group 
		extends AbstractCommonEntity<Long>
		implements Serializable {

	
	private static final long serialVersionUID = -1225569247249272977L;


	/**
     * Default object description for message composition
     */
    public static final String DEFAULT_DESCRIPTION = "group";
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    
    @Column(name = "name",
            length = 40,
            nullable = false,
            updatable = true,
            unique = true)
    @NotBlank(message = "name must not be null, nor empty")
    @Size(min = 3, max = 40, message = "name must contain between 3 and 20 characters")
    private String name;
    
    
    @Column(name = "description",
            length = 1000,
            nullable = true,
            updatable = true)
    private String description;
    
    
    @EqualsAndHashCode.Exclude
	@OneToMany(mappedBy="group", fetch = FetchType.LAZY)
	private Set<UserGroup> userGroup = new HashSet<>();
    
}
