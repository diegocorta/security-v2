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
import pm.security.v2.api.controller.PermissionController;
import pm.security.v2.api.entity.Permission;
import pm.security.v2.common.dto.PermissionDto;
import pm.security.v2.common.dto.min.PermissionMinDto;

public class PermissionAssembler 
		implements IAssemblerMinificable<Permission, PermissionDto, PermissionMinDto> {

	private static final PermissionAssembler INSTANCE = new PermissionAssembler();

    private PermissionAssembler() {}

    public static PermissionAssembler getInstance() {
        return INSTANCE;
    }

	@Override
	public Permission buildEntityFromDto(PermissionDto dto, JoinEntityMap relatedEntities) {
		
		Permission entity = new Permission();
		BeanUtils.copyProperties(dto, entity);
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		return entity;
		
	}  
    
	@Override
	public PermissionDto buildDtoFromEntity(Permission entity) {
		
		PermissionDto dto = new PermissionDto();
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
	public EntityModel<PermissionDto> buildDtoWithLinksFromEntity(
			@NotNull Permission entity) {

		PermissionDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<PermissionDto> dtoWithLinks = EntityModel.of(dto);

		dtoWithLinks.add(
				linkTo( methodOn(PermissionController.class).getPermission(entity.getId()) )
						.withSelfRel() );
		dtoWithLinks.add(
				linkTo( methodOn(PermissionController.class).getPermissionMinified(entity.getId()) )
				.withRel("minimized") );

		return dtoWithLinks;
	}
	
	@Override
	public Collection<EntityModel<PermissionDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<Permission> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}
	
}