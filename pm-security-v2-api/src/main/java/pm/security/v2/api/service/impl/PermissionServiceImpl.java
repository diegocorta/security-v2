package pm.security.v2.api.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import es.common.service.BasicMinificableService;
import es.common.util.JoinEntityMap;
import es.common.util.MessageUtils;
import jakarta.persistence.EntityExistsException;
import pm.security.v2.api.assembler.PermissionAssembler;
import pm.security.v2.api.entity.Permission;
import pm.security.v2.api.repository.IPermissionRepository;
import pm.security.v2.api.service.IPermissionService;
import pm.security.v2.common.dto.PermissionDto;
import pm.security.v2.common.dto.min.PermissionMinDto;

@Service
public class PermissionServiceImpl extends BasicMinificableService<IPermissionRepository, Permission, Long, PermissionDto, PermissionMinDto, PermissionAssembler>
		implements IPermissionService {
	
	public PermissionServiceImpl(IPermissionRepository repository) {
		super(Permission.class, PermissionMinDto.class, repository, PermissionAssembler.getInstance());
	}

	@Override
	public Map<PermissionDto, JoinEntityMap> getRelatedEntities(Collection<PermissionDto> dtos) {
		
		return null;
	}

	@Override
	public void basicDataValidation(Collection<PermissionDto> dtos) {
		
		for (PermissionDto dto : dtos) {
			Permission permission = new Permission();
			permission.setName(dto.getName());
			
			Optional<Permission> prevPermission = repository.findOne(Example.of(permission));
			boolean nameDuplicated = false;
			
			if (nameDuplicated) {
				
				Assert.isNull(dto.getId(), MessageUtils.identifierMustBeNull(Permission.DEFAULT_DESCRIPTION));
				if (prevPermission.isPresent()) nameDuplicated = true;
				
			} else {
				
				Assert.notNull(dto.getId(), MessageUtils.identifierMustNotBeNull(Permission.DEFAULT_DESCRIPTION));
				if (prevPermission.isPresent() && !prevPermission.get().getId().equals(dto.getId()))
					nameDuplicated = true;
				
			}
			
			if (nameDuplicated) {
				throw new EntityExistsException(MessageUtils.entityAlrreadyExistsExceptionMessage(Permission.DEFAULT_DESCRIPTION));
			}
		}
		
	}

	@Override
	public void createDataValidation(Collection<PermissionDto> dtos) {
		// TODO Auto-generated method stub
		
	}
		
}
 