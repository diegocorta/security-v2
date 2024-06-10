package pm.security.v2.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pm.security.v2.api.entity.Group;

public interface IGroupRepository 
		extends JpaRepository<Group, Long> {

}
