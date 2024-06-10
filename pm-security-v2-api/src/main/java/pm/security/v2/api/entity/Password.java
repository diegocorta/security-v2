package pm.security.v2.api.entity;

import java.io.Serializable;

import es.common.entity.AbstractCommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "pm_password")
public class Password 
		extends AbstractCommonEntity<Long> 
		implements Serializable {

	
	private static final long serialVersionUID = 277982957584680819L;

	/**
     * Default object description for message composition
     */
    public static final String DEFAULT_DESCRIPTION = "permission";
    
    public static final String USER = "user";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    
    @Column(name = "encoded_password",
            length = 100,
            nullable = false,
            updatable = true,
            unique = true)
    @NotBlank(message = "encoded password must not be null, nor empty")
    @Size(min = 3, max = 100, message = "encoded password must contain between 3 and 50 characters")
    private String encodedPassword;
    
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
			foreignKey = @ForeignKey(name = "fk_user_password"),
			nullable = false,
			updatable = false)
    @EqualsAndHashCode.Exclude
    private User user;
	
    
//    @Column(name = "tries_left",
//            nullable = false,
//            updatable = true)
//    private Long tries_left;
//    
//    
//    @Column(name = "blocked",
//            nullable = false,
//            updatable = true)
//    private Boolean blocked;
    
    
    
    
    
}
