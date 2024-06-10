package pm.security.v2.api.service.impl;

import java.security.PrivateKey;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import es.common.util.MessageUtils;
import es.common.util.TokenUtils;
import jakarta.persistence.EntityNotFoundException;
import pm.security.v2.api.entity.Password;
import pm.security.v2.api.entity.RefreshToken;
import pm.security.v2.api.entity.User;
import pm.security.v2.api.exception.PasswordNotMatchesException;
import pm.security.v2.api.repository.IPasswordRepository;
import pm.security.v2.api.repository.IRefreshTokenRepository;
import pm.security.v2.api.repository.IUserRepository;
import pm.security.v2.api.service.IPasswordService;
import pm.security.v2.api.service.IUserAuthenticationService;
import pm.security.v2.api.service.IUserService;
import pm.security.v2.common.dto.JwtResponseDto;
import pm.security.v2.common.dto.PasswordDto;
import pm.security.v2.common.dto.UserEffectiveAuthorizationsDto;
import pm.security.v2.common.dto.UserLoginDto;

@Service
@PropertySource({ "classpath:configuration.properties" })
public class UserAuthenticacionService implements IUserAuthenticationService {

    private final Map<Long, Lock> lockByUser = new ConcurrentHashMap<>();
	
	private final IUserRepository userRepository;
	private final IUserService userService;
	
	private final IPasswordRepository passwordRepository;
	private final IPasswordService passwordService;
	
	private final IRefreshTokenRepository refreshTokenRepository;
	
	private final PasswordEncoder passwordEncoder;
	private final PrivateKey privateKey;
	
	private @Value("${access.token.lifetime.min}") Long accessTokenLifetime;
	private @Value("${refresh.token.lifetime.days}") Long refreshTokenLifetime;

	private @Value("${sessions.per.user}") Long sessionsPerUser;
	private @Value("${max.refresh.tokens.allowed}") Long maxRefreshTokensAllowed;
	
	private @Value("${refresh.token.reuse.allow}") boolean refreshTokenReuse;
	private @Value("${refresh.token.reuse.time.seconds}") Long refreshTokenReuseTime;

	public UserAuthenticacionService(IUserRepository userRepository,
			IPasswordRepository passwordRepository,
			IRefreshTokenRepository refreshTokenRepository,
			IPasswordService passwordService,
			IUserService userService,
			PasswordEncoder passwordEncoder,
			PrivateKey privateKey) {
		
		this.userRepository = userRepository;
		this.userService = userService;

		this.passwordRepository = passwordRepository;
		this.passwordService = passwordService;
		
		this.refreshTokenRepository = refreshTokenRepository;
		
		this.passwordEncoder = passwordEncoder;
		this.privateKey = privateKey;
		
	}
	
	@Override
	public JwtResponseDto login(UserLoginDto dto) {
		
		try {
			User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new EntityNotFoundException(
					MessageUtils.entityNotFoundExceptionMessage(User.DEFAULT_DESCRIPTION)));
	
			Password password = passwordRepository.findFirstByUserUsernameAndActiveTrue(dto.getUsername())
					.orElseThrow(() -> new EntityNotFoundException(
							MessageUtils.entityNotFoundExceptionMessage(Password.DEFAULT_DESCRIPTION)));
	
			Assert.isTrue(passwordEncoder.matches(dto.getPassword(), password.getEncodedPassword()), "");
	
			String refreshToken = createRefreshToken(user);
			
			return generateToken(user, refreshToken);
		
		} catch (Exception e) {
			throw new PasswordNotMatchesException();
		}
		
	}
	
	@Override
	public PasswordDto createPassword(PasswordDto password) {
		
		password.setEncodedPassword(
				passwordEncoder.encode(password.getPassword()));
		
		return passwordService.save(password).getContent();
		
	}
	
	
	@Override
	public JwtResponseDto refresh(UUID refreshToken) {
		
		RefreshToken refresh = refreshTokenRepository.findById(refreshToken)
				.orElseThrow(() -> new RuntimeException("INVALID REFRESH"));
		
		User user = userRepository.findById(refresh.getUser().getId()).get();
		
		Lock userLock = lockByUser.computeIfAbsent(user.getId(), k -> new ReentrantLock());
        userLock.lock();
        
        // Receive the latest status of the entity after the lock starts
        refresh = refreshTokenRepository.findById(refreshToken)
				.orElseThrow(() -> new RuntimeException("INVALID REFRESH"));
        
        try {
            
        	if (refresh.getActive()) {
    			
    			refresh.setActive(false);
    			refresh.setUsedAt(ZonedDateTime.now());
    			
    			refreshTokenRepository.save(refresh);
    			
    			// Generate the new token and return the identifier
    			RefreshToken newRefreshToken = new RefreshToken();
    			newRefreshToken.setExpireAt(ZonedDateTime.now()
    					.plusDays(refreshTokenLifetime));
    			newRefreshToken.setUser(user);
    			newRefreshToken.setFamilyNum(refresh.getFamilyNum());
    			
    			newRefreshToken = refreshTokenRepository.save(newRefreshToken);
    			
    			return generateToken(user, newRefreshToken.getId().toString());
    					
    		} else if (refreshTokenReuse &&
    				!refresh.getActive() &&
    				refresh.getUsedAt().plusSeconds(refreshTokenReuseTime).isAfter(ZonedDateTime.now())) {

    			RefreshToken newRefreshToken = refreshTokenRepository.findFirstByUserIdOrderByFamilyNumDesc(refresh.getUser().getId());
    			
    			return generateToken(user, newRefreshToken.getId().toString());
    		
    		} else {
    			
    			refreshTokenRepository.deleteAllInBatch(
    					refreshTokenRepository.findAllByUserIdAndFamilyNum(user.getId(), refresh.getFamilyNum()));
    		
    			throw new RuntimeException("INVALID REFRESH");
    		}
        	
        } finally {
        	
            userLock.unlock();
        }
		
		
		
	}
	
	
	private String createRefreshToken(User user) {
		
		Long lastFamily = 0L;
		
		// Obtaining all refresh tokens from users
		TreeSet<Long> familyNums = refreshTokenRepository.findAllFamilyMunsByUser(user.getId())
				.stream().collect(Collectors.toCollection(TreeSet::new));
		
		if (familyNums.size() > 0)
			lastFamily = familyNums.last();
		
		// Obtain a list of elements to remove, the 5 biggest can't be removed
		if (familyNums.size() >= sessionsPerUser) {
			
			for (int i = 1; i < sessionsPerUser; i++) {
				familyNums.remove(familyNums.last());
			}
			
		} else {
			familyNums.clear();
		}
		
		
		for (Long familyNum : familyNums) {
			refreshTokenRepository.deleteAllInBatch(
					refreshTokenRepository.findAllByUserIdAndFamilyNum(user.getId(), familyNum));
		}		
		
		// Generate the new token and return the identifier
		RefreshToken newRefreshToken = new RefreshToken();
		newRefreshToken.setExpireAt(ZonedDateTime.now()
				.plusDays(refreshTokenLifetime));
		newRefreshToken.setUser(user);
		newRefreshToken.setFamilyNum(++lastFamily);
		
		
		return refreshTokenRepository.save(newRefreshToken)
				.getId().toString();
		
	}
	
	private JwtResponseDto generateToken(User user, String refreshToken) {
		
		UserEffectiveAuthorizationsDto permissions = userService.findUserEffectivePermissions(user.getUsername());

		Map<String, Object> additionalClaims = new HashMap<>();
		
		additionalClaims.put(TokenUtils.SECURITY_USER,  user.getId());
		additionalClaims.put(TokenUtils.ROLES, permissions.getRoles());
		additionalClaims.put(TokenUtils.PERMISSIONS_GRANTED, permissions.getGrantedPermissions());
		additionalClaims.put(TokenUtils.PERMISSIONS_REVOKED, permissions.getRevokedPermissions());

		String token = TokenUtils.generateTokenFromUsername(
				user.getUsername(),
				"PM",
				ZonedDateTime.now(),
				accessTokenLifetime * 60,
				additionalClaims,
				privateKey);

		JwtResponseDto response = new JwtResponseDto();
		response.setToken(token);
		response.setRefreshToken(refreshToken);
		response.setType("Bearer");
		
		return response;
	}

}
