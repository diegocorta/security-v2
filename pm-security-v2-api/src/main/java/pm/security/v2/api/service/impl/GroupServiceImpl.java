package pm.security.v2.api.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import es.common.service.BasicMinificableService;
import es.common.util.MessageUtils;
import es.common.util.JoinEntityMap;
import jakarta.persistence.EntityExistsException;
import pm.security.v2.api.assembler.GroupAssembler;
import pm.security.v2.api.entity.Group;
import pm.security.v2.api.repository.IGroupRepository;
import pm.security.v2.api.service.IGroupService;
import pm.security.v2.common.dto.GroupDto;
import pm.security.v2.common.dto.min.GroupMinDto;

@Service
public class GroupServiceImpl extends BasicMinificableService<IGroupRepository, Group, Long, GroupDto, GroupMinDto, GroupAssembler>
		implements IGroupService {
	
	public GroupServiceImpl(IGroupRepository repository) {
		super(Group.class, GroupMinDto.class, repository, GroupAssembler.getInstance());
	}

	@Override
	public Map<GroupDto, JoinEntityMap> getRelatedEntities(Collection<GroupDto> dto) {
		
		return null;
	}

	@Override
	public void basicDataValidation(Collection<GroupDto> dtos) {
		
		for (GroupDto dto : dtos) {
			
			Group group = new Group();
			group.setName(dto.getName());
			
			Optional<Group> prevGroup = repository.findOne(Example.of(group));
			boolean nameDuplicated = false;
			
	
			if (nameDuplicated) {
			
				Assert.isNull(dto.getId(), MessageUtils.identifierMustBeNull(Group.DEFAULT_DESCRIPTION));
				if (prevGroup.isPresent()) nameDuplicated = true;
				
			} else {
				
				Assert.notNull(dto.getId(), MessageUtils.identifierMustNotBeNull(Group.DEFAULT_DESCRIPTION));
				if (prevGroup.isPresent() && !prevGroup.get().getId().equals(dto.getId()))
					nameDuplicated = true;
				
			}
			
			if (nameDuplicated) {
				throw new EntityExistsException(MessageUtils.entityAlrreadyExistsExceptionMessage(Group.DEFAULT_DESCRIPTION));
			}
		}
		
	}

	@Override
	public void createDataValidation(Collection<GroupDto> dtos) {
				
	}
	
}
 