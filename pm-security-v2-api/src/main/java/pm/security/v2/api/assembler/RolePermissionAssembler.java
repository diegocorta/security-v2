package pm.security.v2.api.assembler;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;

import es.common.assembler.IAssembler;
import es.common.util.AssemblerUtil;
import es.common.util.JoinEntityMap;
import jakarta.validation.constraints.NotNull;
import pm.security.v2.api.entity.Permission;
import pm.security.v2.api.entity.Role;
import pm.security.v2.api.entity.RolePermission;
import pm.security.v2.api.entity.embedded.key.RolePermissionId;
import pm.security.v2.common.dto.RolePermissionDto;

public class RolePermissionAssembler implements IAssembler<RolePermission, RolePermissionDto>{

	private static final RolePermissionAssembler INSTANCE = new RolePermissionAssembler();

    private RolePermissionAssembler() {}

    public static RolePermissionAssembler getInstance() {
        return INSTANCE;
    }
	
	@Override
	public RolePermission buildEntityFromDto(RolePermissionDto dto, JoinEntityMap relatedEntities) {
		
		RolePermission entity = new RolePermission();
		BeanUtils.copyProperties(dto, entity);
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		Permission permission = relatedEntities.get(RolePermission.PERMISSION, Permission.class);
		Role role = relatedEntities.get(RolePermission.ROLE, Role.class);
		
		RolePermissionId rolePermissionId = new RolePermissionId();
		rolePermissionId.setRoleId(role.getId());
		rolePermissionId.setPermissionId(permission.getId());
		
		entity.setId(rolePermissionId);
		entity.setRole(role);
		entity.setPermission(permission);
		
		return entity;
	}

	@Override
	public RolePermissionDto buildDtoFromEntity(RolePermission entity) {
		
		RolePermissionDto dto = new RolePermissionDto();
		BeanUtils.copyProperties(entity, dto);
		
		AssemblerUtil.copyBasicPropertiesToDto(entity, dto);

		dto.setRoleId(entity.getId().getRoleId());
		dto.setPermissionId(entity.getId().getPermissionId());
		
		return dto;
	}

	@Override
	public EntityModel<RolePermissionDto> buildDtoWithLinksFromEntity(@NotNull RolePermission entity) {
		
		RolePermissionDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<RolePermissionDto> dtoWithLinks = EntityModel.of(dto);

//		dtoWithLinks.add(
//				linkTo( methodOn(RolePermissionController.class).getUser(entity.getId()) )
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
	public Collection<EntityModel<RolePermissionDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<RolePermission> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}

}
