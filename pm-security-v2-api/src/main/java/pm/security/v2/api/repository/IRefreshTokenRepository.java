package pm.security.v2.api.repository;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pm.security.v2.api.entity.RefreshToken;

public interface IRefreshTokenRepository 
		extends JpaRepository<RefreshToken, UUID> {
	
	Collection<RefreshToken> findAllByUserId(Long userId);
	
	
	RefreshToken findFirstByUserIdOrderByFamilyNumDesc(Long userId);
	
	
	Collection<RefreshToken> findAllByUserIdAndFamilyNum(Long userId, Long familyNum);
	
	
	Long deleteByUserIdAndFamilyNum(Long userId, Long familyNum);
	
	
	@Query(value = "SELECT distinct(u.familyNum) FROM RefreshToken u")
	Set<Long> findAllFamilyMunsByUser(Long userId);
		
}
