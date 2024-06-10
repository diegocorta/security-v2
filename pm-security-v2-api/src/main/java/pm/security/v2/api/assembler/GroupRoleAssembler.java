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
import pm.security.v2.api.entity.GroupRole;
import pm.security.v2.api.entity.Role;
import pm.security.v2.api.entity.embedded.key.GroupRoleId;
import pm.security.v2.common.dto.GroupRoleDto;

public class GroupRoleAssembler implements IAssembler<GroupRole, GroupRoleDto>{

	private static final GroupRoleAssembler INSTANCE = new GroupRoleAssembler();

    private GroupRoleAssembler() {}

    public static GroupRoleAssembler getInstance() {
        return INSTANCE;
    }
	
	@Override
	public GroupRole buildEntityFromDto(GroupRoleDto dto, JoinEntityMap relatedEntities) {
		
		GroupRole entity = new GroupRole();
		BeanUtils.copyProperties(dto, entity);
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		Group permission = relatedEntities.get(GroupRole.GROUP, Group.class);
		Role role = relatedEntities.get(GroupRole.ROLE, Role.class);
		
		GroupRoleId groupRoleId = new GroupRoleId();
		groupRoleId.setRoleId(role.getId());
		groupRoleId.setGroupId(permission.getId());
		
		entity.setId(groupRoleId);
		entity.setRole(role);
		entity.setGroup(permission);
		
		return entity;
	}

	@Override
	public GroupRoleDto buildDtoFromEntity(GroupRole entity) {
		
		GroupRoleDto dto = new GroupRoleDto();
		BeanUtils.copyProperties(entity, dto);
		
		AssemblerUtil.copyBasicPropertiesToDto(entity, dto);

		dto.setRoleId(entity.getId().getRoleId());
		dto.setGroupId(entity.getId().getGroupId());
		
		return dto;
	}

	@Override
	public EntityModel<GroupRoleDto> buildDtoWithLinksFromEntity(@NotNull GroupRole entity) {
		
		GroupRoleDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<GroupRoleDto> dtoWithLinks = EntityModel.of(dto);

//		dtoWithLinks.add(
//				linkTo( methodOn(GroupRoleController.class).getUser(entity.getId()) )
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
	public Collection<EntityModel<GroupRoleDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<GroupRole> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}

}
