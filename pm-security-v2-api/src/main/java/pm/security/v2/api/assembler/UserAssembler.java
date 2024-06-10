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
import pm.security.v2.api.controller.UserController;
import pm.security.v2.api.controller.UserGroupController;
import pm.security.v2.api.entity.User;
import pm.security.v2.common.dto.UserDto;
import pm.security.v2.common.dto.min.UserMinDto;

public class UserAssembler 
		implements IAssemblerMinificable<User, UserDto, UserMinDto> {

	private static final UserAssembler INSTANCE = new UserAssembler();

    private UserAssembler() {}

    public static UserAssembler getInstance() {
        return INSTANCE;
    }

	@Override
	public User buildEntityFromDto(UserDto dto, JoinEntityMap relatedEntities) {
		
		User entity = new User();
		BeanUtils.copyProperties(dto, entity);
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		return entity;
		
	}  
    
	@Override
	public UserDto buildDtoFromEntity(User entity) {
		
		UserDto dto = new UserDto();
		BeanUtils.copyProperties(entity, dto);
		
		AssemblerUtil.copyBasicPropertiesToDto(entity, dto);
		
		if (entity.getPassword() != null) {
			dto.setHasPassword(true);
		} else {
			dto.setHasPassword(false);
		}

		return dto;
		
	}

	/**
	 * Converts the specified entity representation to the corresponding HATEOAS representation of information
	 *
	 * @param dto complete representation to which we are going to add the HATEOAS links
	 * @return the representation of information created from the specified domain entity
	 */
	@Override
	public EntityModel<UserDto> buildDtoWithLinksFromEntity(
			@NotNull User entity) {

		UserDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<UserDto> dtoWithLinks = EntityModel.of(dto);

		dtoWithLinks.add(
				linkTo( methodOn(UserController.class).getUser(entity.getId()) )
						.withSelfRel() );
		dtoWithLinks.add(
				linkTo( methodOn(UserController.class).getUserMinified(entity.getId()) )
				.withRel(UserDto.MINIMIZED_REL) );

		dtoWithLinks.add(
				linkTo( methodOn(UserGroupController.class).getGroupsOfUser(entity.getId()) )
				.withRel(UserDto.GROUP_REL) );
		
		dtoWithLinks.add(
				linkTo( methodOn(UserController.class).getRolesOfUser(entity.getId()) )
				.withRel(UserDto.ROLES_REL) );

		return dtoWithLinks;
	}
	
	@Override
	public Collection<EntityModel<UserDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<User> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}
	
}