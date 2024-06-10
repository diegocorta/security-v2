package pm.security.v2.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;

import es.common.assembler.IAssembler;
import es.common.util.AssemblerUtil;
import es.common.util.JoinEntityMap;
import jakarta.validation.constraints.NotNull;
import pm.security.v2.api.controller.GroupController;
import pm.security.v2.api.controller.UserController;
import pm.security.v2.api.entity.Group;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.entity.UserGroup;
import pm.security.v2.api.entity.embedded.key.UserGroupId;
import pm.security.v2.common.dto.UserGroupDto;

public class UserGroupAssembler implements IAssembler<UserGroup, UserGroupDto>{

	private static final UserGroupAssembler INSTANCE = new UserGroupAssembler();

    private UserGroupAssembler() {}

    public static UserGroupAssembler getInstance() {
        return INSTANCE;
    }
	
	@Override
	public UserGroup buildEntityFromDto(UserGroupDto dto, JoinEntityMap relatedEntities) {
		
		UserGroup entity = new UserGroup();
		BeanUtils.copyProperties(dto, entity);
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		Group group = relatedEntities.get(UserGroup.GROUP, Group.class);
		User user = relatedEntities.get(UserGroup.USER, User.class);
		
		UserGroupId userGroupId = new UserGroupId();
		userGroupId.setGroupId(group.getId());
		userGroupId.setUserId(user.getId());
		
		entity.setId(userGroupId);
		entity.setGroup(group);
		entity.setUser(user);
		
		return entity;
	}

	@Override
	public UserGroupDto buildDtoFromEntity(UserGroup entity) {
		
		UserGroupDto dto = new UserGroupDto();
		BeanUtils.copyProperties(entity, dto);
		
		AssemblerUtil.copyBasicPropertiesToDto(entity, dto);

		dto.setGroupId(entity.getId().getGroupId());
		dto.setUserId(entity.getId().getUserId());
		
		return dto;
	}

	@Override
	public EntityModel<UserGroupDto> buildDtoWithLinksFromEntity(@NotNull UserGroup entity) {
		
		UserGroupDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<UserGroupDto> dtoWithLinks = EntityModel.of(dto);

//		dtoWithLinks.add(
//				linkTo( methodOn(UserGroupController.class).getUser(entity.getId()) )
//						.withSelfRel() );
		dtoWithLinks.add(
				linkTo( methodOn(UserController.class).getUser(dto.getUserId()) )
						.withRel("user") );
		dtoWithLinks.add(
				linkTo( methodOn(GroupController.class).getGroup(dto.getGroupId()) )
						.withRel("group") );

		return dtoWithLinks;
		
	}

	@Override
	public Collection<EntityModel<UserGroupDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<UserGroup> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}

}
