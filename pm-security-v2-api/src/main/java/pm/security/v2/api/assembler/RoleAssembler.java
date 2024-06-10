package pm.security.v2.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;

import es.common.assembler.IAssemblerMinificable;
import es.common.util.AssemblerUtil;
import es.common.util.JoinEntityMap;
import jakarta.validation.constraints.NotNull;
import pm.security.v2.api.controller.RoleController;
import pm.security.v2.api.controller.RoleGroupController;
import pm.security.v2.api.entity.Role;
import pm.security.v2.common.dto.RoleDto;
import pm.security.v2.common.dto.min.RoleMinDto;

public class RoleAssembler 
		implements IAssemblerMinificable<Role, RoleDto, RoleMinDto> {

	private static final RoleAssembler INSTANCE = new RoleAssembler();

    private RoleAssembler() {}

    public static RoleAssembler getInstance() {
        return INSTANCE;
    }

	@Override
	public Role buildEntityFromDto(RoleDto dto, JoinEntityMap relatedEntities) {
		
		Role entity = new Role();
		BeanUtils.copyProperties(dto, entity);
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		return entity;
		
	}  
    
	@Override
	public RoleDto buildDtoFromEntity(Role entity) {
		
		RoleDto dto = new RoleDto();
		BeanUtils.copyProperties(entity, dto);
		
		if (entity.getParentRole() != null) {
			dto.setParentRoleId(entity.getParentRole().getId());
		}
		
		AssemblerUtil.copyBasicPropertiesToDto(entity, dto);

		return dto;
		
	}

	/**
	 * Converts the specified entity representation to the corresponding HATEOAS representation of information
	 *
	 * @param dto complete representation to which we are going to add the HATEOAS links
	 * @return the representation of information created from the specified domain entity
	 */
	@Override
	public EntityModel<RoleDto> buildDtoWithLinksFromEntity(
			@NotNull Role entity) {

		RoleDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<RoleDto> dtoWithLinks = EntityModel.of(dto);

		dtoWithLinks.add(
				linkTo( methodOn(RoleController.class).getRole(entity.getId()) )
						.withSelfRel() );
		dtoWithLinks.add(
				linkTo( methodOn(RoleController.class).getRoleMinified(entity.getId()) )
				.withRel(RoleDto.MINIMIZED_REL) );
		dtoWithLinks.add(
				linkTo( methodOn(RoleGroupController.class).getGroupsOfRole(entity.getId()) )
				.withRel(RoleDto.GROUPS_REL) );
		dtoWithLinks.add(
				linkTo( methodOn(RoleController.class).getUsersOfRole(entity.getId()) )
				.withRel(RoleDto.USERS_REL) );
		
		if (dto.getParentRoleId() != null) {
			dtoWithLinks.add(
					linkTo( methodOn(RoleController.class).getRole(dto.getParentRoleId()) )
					.withRel(RoleDto.PARENT_REL) );
		}

		return dtoWithLinks;
	}
	
	@Override
	public Collection<EntityModel<RoleDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<Role> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}
	
}