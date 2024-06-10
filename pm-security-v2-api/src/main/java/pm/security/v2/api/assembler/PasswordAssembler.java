package pm.security.v2.api.assembler;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;

import es.common.assembler.IAssembler;
import es.common.util.AssemblerUtil;
import es.common.util.JoinEntityMap;
import jakarta.validation.constraints.NotNull;
import pm.security.v2.api.entity.Password;
import pm.security.v2.api.entity.User;
import pm.security.v2.common.dto.PasswordDto;

public class PasswordAssembler 
		implements IAssembler<Password, PasswordDto> {

	private static final PasswordAssembler INSTANCE = new PasswordAssembler();

    private PasswordAssembler() {}

    public static PasswordAssembler getInstance() {
        return INSTANCE;
    }

	@Override
	public Password buildEntityFromDto(PasswordDto dto, JoinEntityMap relatedEntities) {
		
		Password entity = new Password();
		BeanUtils.copyProperties(dto, entity);		
		
		AssemblerUtil.copyBasicPropertiesToEntity(dto, entity);
		
		User user = relatedEntities.get(Password.USER, User.class);

		entity.setUser(user);
		
		return entity;
		
	}  
    
	@Override
	public PasswordDto buildDtoFromEntity(Password entity) {
		
		PasswordDto dto = new PasswordDto();
		BeanUtils.copyProperties(entity, dto);
		
		AssemblerUtil.copyBasicPropertiesToDto(entity, dto);
		
		dto.setUserId(entity.getUser().getId());
		
		return dto;
		
	}

	/**
	 * Converts the specified entity representation to the corresponding HATEOAS representation of information
	 *
	 * @param dto complete representation to which we are going to add the HATEOAS links
	 * @return the representation of information created from the specified domain entity
	 */
	@Override
	public EntityModel<PasswordDto> buildDtoWithLinksFromEntity(
			@NotNull Password entity) {

		PasswordDto dto = buildDtoFromEntity(entity);
		
		// Add resource relations
		EntityModel<PasswordDto> dtoWithLinks = EntityModel.of(dto);

		return dtoWithLinks;
	}
	
	@Override
	public Collection<EntityModel<PasswordDto>> buildDtosWithLinksFromEntities(
			@NotNull Collection<Password> entities) {
		
		return entities
				.stream()
				.map(entity -> buildDtoWithLinksFromEntity(entity) )
				.collect( Collectors.toList() );
		
	}
	
}