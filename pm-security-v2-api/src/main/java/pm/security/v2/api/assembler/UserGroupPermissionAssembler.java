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
import pm.security.v2.api.entity.Permission;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.entity.UserGroupPermission;
import pm.security.v2.api.entity.embedded.key.UserGroupPermissionId;
import pm.security.v2.common.dto.UserGroupPermissionDto;

public class UserGroupPermissionAssembler implements IAssembler<UserGroupPermission, UserGroupPermissionDto>{

	private static final UserGroupPermissionAssembler INSTANCE = new UserGroupPermissionAssembler();

    private UserGroupPermissionAssembler() {}

    public static UserGroupPermissionAssembler getInstance() {
        return INSTANCE;
    }
	
	@Override
	public UserGroupPermission buildEntityFromDto(UserGroupPermissionDto dto, JoinEntityMap relatedEntities) {
		
		UserGroupPermission entity = new UserGroupPermission();
		BeanUtils.copyProperties(dto, entity);
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		Group group = relatedEntities.get(UserGroupPermission.GROUP, Group.class);
		User user = relatedEntities.get(UserGroupPermission.USER, User.class);
		Permission permission = relatedEntities.get(UserGroupPermission.PERMISSION, Permission.class);
		
		
		UserGroupPermissionId userGroupPermissionId = new UserGroupPermissionId();
		userGroupPermissionId.setGroupId(group.getId());
		userGroupPermissionId.setUserId(user.getId());
		userGroupPermissionId.setPermissionId(permission.getId());
		
		entity.setId(userGroupPermissionId);
		entity.setGroup(group);
		entity.setUser(user);
		entity.setPermission(permission);
		
		return entity;
	}

	@Override
	public UserGroupPermissionDto buildDtoFromEntity(UserGroupPermission entity) {
		
		UserGroupPermissionDto dto = new UserGroupPermissionDto();
		BeanUtils.copyProperties(entity, dto);
		
		AssemblerUtil.copyBasicPropertiesToDto(entity, dto);

		dto.setGroupId(entity.getId().getGroupId());
		dto.setUserId(entity.getId().getUserId());
		dto.setPermissionId(entity.getId().getPermissionId());
		
		return dto;
	}

	@Override
	public EntityModel<UserGroupPermissionDto> buildDtoWithLinksFromEntity(@NotNull UserGroupPermission entity) {
		
		UserGroupPermissionDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<UserGroupPermissionDto> dtoWithLinks = EntityModel.of(dto);

//		dtoWithLinks.add(
//				linkTo( methodOn(UserGroupPermissionController.class).getUser(entity.getId()) )
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
	public Collection<EntityModel<UserGroupPermissionDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<UserGroupPermission> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}

}
