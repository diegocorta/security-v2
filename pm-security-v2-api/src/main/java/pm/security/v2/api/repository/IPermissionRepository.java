package pm.security.v2.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pm.security.v2.api.entity.Permission;

public interface IPermissionRepository 
		extends JpaRepository<Permission, Long> {

}
