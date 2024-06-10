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
import pm.security.v2.api.controller.GroupController;
import pm.security.v2.api.controller.GroupRoleController;
import pm.security.v2.api.controller.GroupUserController;
import pm.security.v2.api.entity.Group;
import pm.security.v2.common.dto.GroupDto;
import pm.security.v2.common.dto.min.GroupMinDto;

public class GroupAssembler 
		implements IAssemblerMinificable<Group, GroupDto, GroupMinDto> {

	private static final GroupAssembler INSTANCE = new GroupAssembler();

    private GroupAssembler() {}

    public static GroupAssembler getInstance() {
        return INSTANCE;
    }

	@Override
	public Group buildEntityFromDto(GroupDto dto, JoinEntityMap relatedEntities) {
		
		Group entity = new Group();
		BeanUtils.copyProperties(dto, entity);
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		return entity;
		
	}  
    
	@Override
	public GroupDto buildDtoFromEntity(Group entity) {
		
		GroupDto dto = new GroupDto();
		BeanUtils.copyProperties(entity, dto);
		
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
	public EntityModel<GroupDto> buildDtoWithLinksFromEntity(
			@NotNull Group entity) {

		GroupDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<GroupDto> dtoWithLinks = EntityModel.of(dto);

		dtoWithLinks.add(
				linkTo( methodOn(GroupController.class).getGroup(entity.getId()) )
						.withSelfRel() );
		dtoWithLinks.add(
				linkTo( methodOn(GroupController.class).getGroupMinified(entity.getId()) )
				.withRel(GroupDto.MINIMIZED_REL) );
		
		dtoWithLinks.add(
				linkTo( methodOn(GroupUserController.class).getUsersOfGroup(entity.getId()) )
				.withRel(GroupDto.USERS_REL) );		
		dtoWithLinks.add(
				linkTo( methodOn(GroupRoleController.class).getRolesOfGroup(entity.getId()) )
				.withRel(GroupDto.ROLES_REL) );

		return dtoWithLinks;
	}
	
	@Override
	public Collection<EntityModel<GroupDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<Group> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}
	
}