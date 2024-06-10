package pm.security.v2.api.configuration;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.JsonFormatter;

import es.common.dto.ErrorResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private @Value("${allowed.origins}")List<String> corsOrigins;
	
	@Autowired
	private JwtTokenFilter jwtTokenFilter;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		return http
				// Cross Site Request Forgery. Is used on session based environments
				.csrf(csrf -> csrf.disable())
				// Cross-Origin Resource Sharing
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				// XSS protection
				.headers(headers ->
                headers.xssProtection(
                        xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                ).contentSecurityPolicy(
                        cps -> cps.policyDirectives("script-src 'self'")
                ))
				.exceptionHandling(exceptionHandling -> exceptionHandling
	                    .authenticationEntryPoint(customAuthenticationEntryPoint())
	                    .accessDeniedHandler(customAccessDeniedHandler())
	            )
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/swagger-ui/**",
                                "/api-docs/**",
                                "**/users/login",
                                "**/users/refresh",
                                "/error").permitAll()
						.anyRequest().authenticated()
						)
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.build();

	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Argon2PasswordEncoder(16, 32, 1, 20000, 6);    
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedOrigins(corsOrigins);
		configuration.addAllowedHeader("*");
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public AuthenticationEntryPoint customAuthenticationEntryPoint() {
	    return new AuthenticationEntryPoint() {
	        @Override
	        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
	                  		
	        	ErrorResponseDto err = new ErrorResponseDto();	        	
	        	err.setCode(401);
	        	err.setException("UnauthorizedException. " + (String) request.getAttribute("TOKEN_ERROR"));
	        	err.setMessage("Must be authorithed to access this resources. Login first");
	        	err.setTimestamp(ZonedDateTime.now());
	        	
	        	response.setStatus(401);
	        	response.setContentType("application/json;charset=UTF-8");
	        	response.getWriter().write(getResponse(err));

	        }
	    };
	}
	        	
	@Bean
	public AccessDeniedHandler customAccessDeniedHandler() {
		return new AccessDeniedHandler() {
	        @Override
	        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
	        	
	        	ErrorResponseDto err = new ErrorResponseDto();
	        	err.setCode(403);
	        	err.setException("UnauthorizedException");     
	        	err.setMessage("DENIED. " + accessDeniedException.getLocalizedMessage());
	        	err.setTimestamp(ZonedDateTime.now());

	        	
	        	response.setStatus(403);
	        	response.setContentType("application/json;charset=UTF-8");
	        	response.getWriter().write(getResponse(err));
	        	
	        }
	    };
	}  
	
	private String getResponse(ErrorResponseDto errorResponse) throws IOException {
	    
//		ObjectMapper objectMapper = JsonMapper.builder()
//			    .findAndAddModules()
//			    .build();	
		
		String errorResponseJson = objectMapper.writeValueAsString(errorResponse);
	    
		return JsonFormatter.prettyPrint(errorResponseJson);
		
	}
	
}
