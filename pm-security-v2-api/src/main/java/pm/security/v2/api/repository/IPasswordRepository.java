package pm.security.v2.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pm.security.v2.api.entity.Password;

public interface IPasswordRepository 
		extends JpaRepository<Password, Long> {

	public Optional<Password> findFirstByUserUsernameAndActiveTrue(String username);

}
