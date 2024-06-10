package pm.security.v2.api.assembler;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;

import es.common.assembler.IAssembler;
import es.common.util.AssemblerUtil;
import es.common.util.JoinEntityMap;
import jakarta.validation.constraints.NotNull;
import pm.security.v2.api.entity.Group;
import pm.security.v2.api.entity.Role;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.entity.UserGroupRole;
import pm.security.v2.api.entity.embedded.key.UserGroupRoleId;
import pm.security.v2.common.dto.UserGroupRoleDto;

public class UserGroupRoleAssembler implements IAssembler<UserGroupRole, UserGroupRoleDto>{

	private static final UserGroupRoleAssembler INSTANCE = new UserGroupRoleAssembler();

    private UserGroupRoleAssembler() {}

    public static UserGroupRoleAssembler getInstance() {
        return INSTANCE;
    }
	
	@Override
	public UserGroupRole buildEntityFromDto(UserGroupRoleDto dto, JoinEntityMap relatedEntities) {
		
		UserGroupRole entity = new UserGroupRole();
		BeanUtils.copyProperties(dto, entity);
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		Group group = relatedEntities.get(UserGroupRole.GROUP, Group.class);
		User user = relatedEntities.get(UserGroupRole.USER, User.class);
		Role role = relatedEntities.get(UserGroupRole.ROLE, Role.class);
		
		UserGroupRoleId userGroupRoleId = new UserGroupRoleId();
		userGroupRoleId.setGroupId(group.getId());
		userGroupRoleId.setUserId(user.getId());
		userGroupRoleId.setRoleId(role.getId());
		
		entity.setId(userGroupRoleId);
		entity.setGroup(group);
		entity.setUser(user);
		entity.setRole(role);
		
		return entity;
	}

	@Override
	public UserGroupRoleDto buildDtoFromEntity(UserGroupRole entity) {
		
		UserGroupRoleDto dto = new UserGroupRoleDto();
		BeanUtils.copyProperties(entity, dto);
		
		AssemblerUtil.copyBasicPropertiesToDto(entity, dto);

		dto.setGroupId(entity.getId().getGroupId());
		dto.setUserId(entity.getId().getUserId());
		dto.setRoleId(entity.getId().getRoleId());
		
		return dto;
	}

	@Override
	public EntityModel<UserGroupRoleDto> buildDtoWithLinksFromEntity(@NotNull UserGroupRole entity) {
		
		UserGroupRoleDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<UserGroupRoleDto> dtoWithLinks = EntityModel.of(dto);

//		dtoWithLinks.add(
//				linkTo( methodOn(UserGroupRoleController.class).getUser(entity.getId()) )
//						.withSelfRel() );
//		dtoWithLinks.add(
//				linkTo( methodOn(UserController.class).getUser(dto.getUserId()) )
//						.withRel("user") );
//		dtoWithLinks.add(
//				linkTo( methodOn(GroupController.class).getGroup(dto.getGroupId()) )
//						.withRel("group") );

		return dtoWithLinks;
		
	}

	@Override
	public Collection<EntityModel<UserGroupRoleDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<UserGroupRole> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}

}
