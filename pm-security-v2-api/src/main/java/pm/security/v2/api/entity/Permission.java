package pm.security.v2.api.entity;

import java.io.Serializable;

import es.common.entity.AbstractCommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "pm_permission")
public class Permission 
		extends AbstractCommonEntity<Long>
		implements Serializable {

	
	private static final long serialVersionUID = 8124988976227562121L;


	/**
     * Default object description for message composition
     */
    public static final String DEFAULT_DESCRIPTION = "permission";
    
    
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
    @Size(min = 3, max = 30, message = "name must contain between 3 and 20 characters")
    private String name;
    
    
    @Column(name = "description",
            length = 1000,
            nullable = true,
            updatable = true)
    private String description;
	
}
