package pm.security.v2.api.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

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
import jakarta.persistence.Table;
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
@Table(name = "pm_refresh_token")
public class RefreshToken
		extends AbstractCommonEntity<UUID>
		implements Serializable {

	private static final long serialVersionUID = 7777530386431387949L;

	/**
     * Default object description for message composition
     */
    public static final String DEFAULT_DESCRIPTION = "refresh_token";
     
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",
			nullable = false,
			updatable = false)
	private UUID id;
    
	
	@Column(name = "expire_at", 
			nullable = false)
	private ZonedDateTime expireAt;
	
	@Column(name = "used_at")
	private ZonedDateTime usedAt;
    
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
			foreignKey = @ForeignKey(name = "fk_refresh_token_user"),
			nullable = false,
			updatable = false)
    @EqualsAndHashCode.Exclude
    private User user;
	
	@Column(name = "family_num",
			nullable = false,
			updatable = false)
	private Long familyNum;
	
}
