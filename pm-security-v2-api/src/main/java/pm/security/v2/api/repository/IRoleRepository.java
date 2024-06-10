package pm.security.v2.api.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import pm.security.v2.api.entity.Role;

public interface IRoleRepository extends JpaRepository<Role, Long> {
	
	List<Role> findAllBySonRolesId(Long roleId);
	
	@Transactional(value = TxType.REQUIRED)
	default Set<Role> findAllDescendantsRecursive(Set<Long> roleIds) {

		Set<Role> result = new HashSet<Role>();
		
		List<Role> roles = findAllById(roleIds);
		
		for (Role role : roles) {
			
			result.addAll(childRecursive(result, role));
		}
		
		result.removeIf(role -> roles.contains(role));
		
		return result;
	}

	
	@Transactional(value = TxType.REQUIRED)
	default Set<Role> findAllDescendantsRecursive(Long roleId) {

		Set<Role> result = new HashSet<Role>();
		
		Optional<Role> roleOpt = findById(roleId);

		if (roleOpt.isPresent()) {
			
			Role role = roleOpt.get();
						
			result.addAll(childRecursive(result, role));

		}

		return result;
	}
	
	private Set<Role> childRecursive(Set<Role> result, Role actualRole) {
		
		Set<Role> sonRoles = new HashSet<>(actualRole.getSonRoles());
		
		if (!result.contains(actualRole)) { // Verificar si el rol hijo ya está en el conjunto
			result.add(actualRole);
        }
		
		for (Role sonRole : sonRoles) {
			result.addAll(childRecursive(result, sonRole));
		}
		
		return result;
	}
	
	@Transactional(value = TxType.REQUIRED)
	default Set<Role> findAllAncestorsRecursive(Set<Long> roleIds) {

		Set<Role> result = new HashSet<Role>();
		
		List<Role> roles = findAllById(roleIds);
		
		for (Role role : roles) {
			
			ancestorRecursive(result, role);
		}
		
		return result;
	}

	
	@Transactional(value = TxType.REQUIRED)
	default Set<Role> findAllAncestorsRecursive(Long roleId) {

		Set<Role> result = new HashSet<Role>();
		
		Optional<Role> roleOpt = findById(roleId);

		if (roleOpt.isPresent()) {
			
			Role role = roleOpt.get();
						
			ancestorRecursive(result, role);

		}

		return result;
	}
	
	private void ancestorRecursive(Set<Role> roles, Role actualRole) {
		
		Role parentRole = actualRole.getParentRole();
		
		if (parentRole != null && !roles.contains(parentRole)) {
			
			roles.add(parentRole);
			ancestorRecursive(roles, parentRole);
			
		}
		
	}

}
