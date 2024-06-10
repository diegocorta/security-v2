package pm.security.v2.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pm.security.v2.api.entity.User;

public interface IUserRepository 
		extends JpaRepository<User, Long> {

	public Optional<User> findByUsername(String username);
	
}
