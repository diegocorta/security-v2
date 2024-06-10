package pm.security.v2.api.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import es.common.service.BasicService;
import es.common.util.JoinEntityMap;
import es.common.util.MessageUtils;
import jakarta.persistence.EntityNotFoundException;
import pm.security.v2.api.assembler.PasswordAssembler;
import pm.security.v2.api.entity.Password;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.repository.IPasswordRepository;
import pm.security.v2.api.repository.IUserRepository;
import pm.security.v2.api.service.IPasswordService;
import pm.security.v2.common.dto.PasswordDto;

@Service
public class PasswordServiceImpl extends BasicService<IPasswordRepository, Password, Long, PasswordDto, PasswordAssembler>
		implements IPasswordService {
	
	private final IUserRepository userRepository;

	public PasswordServiceImpl(IPasswordRepository repository,
			IUserRepository userRepository) {
		super(Password.class, repository, PasswordAssembler.getInstance());
		
		this.userRepository = userRepository;
		
	}

	@Override
	public Map<PasswordDto, JoinEntityMap> getRelatedEntities(Collection<PasswordDto> dtos) {
		
		ConcurrentHashMap<PasswordDto, JoinEntityMap> result = new ConcurrentHashMap<>();
		
		Set<Long> userIds = dtos.stream()
				.map(PasswordDto::getUserId)
				.collect(Collectors.toSet());
		
		// Search every entity located
		Map<Long, User> userMap = userRepository.findAllById(userIds)
				.stream()
				.collect(Collectors.toMap(User::getId, Function.identity()));
				
		
		dtos.parallelStream().forEach(dto -> {
			
			User user = userMap.get(dto.getUserId());

			if (user == null) throw new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(User.DEFAULT_DESCRIPTION));
			
			
			Collection<Pair<String, Object>> joinEntityList = List.of(
					Pair.of(Password.USER, user)
			);

			result.put(dto, JoinEntityMap.from(joinEntityList));
			
		});
		
		return result;
		
	}

	@Override
	public void basicDataValidation(Collection<PasswordDto> dtos) {
		
	}

	@Override
	public void createDataValidation(Collection<PasswordDto> dtos) {
		// TODO Auto-generated method stub
		
	}
	
}
 