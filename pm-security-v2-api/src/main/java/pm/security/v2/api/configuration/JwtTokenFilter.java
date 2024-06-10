package pm.security.v2.api.configuration;

import java.io.IOException;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import es.common.util.TokenUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@PropertySource({ "classpath:configuration.properties" })
public class JwtTokenFilter extends OncePerRequestFilter {

	private final PublicKey publicKey;
	
	private @Value("${token.skew.time.seconds}") Long skewSecs;
	
	public JwtTokenFilter(PublicKey publicKey) {
		
		this.publicKey = publicKey;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		String token = getTokenIfExists(request);
		SecurityContextHolder.clearContext();

		if (token != null) {

			try {
				
				Claims claims = TokenUtils.validateToken(token, publicKey, skewSecs);
				
				@SuppressWarnings("unchecked")
				List<String> roles =  (List<String>) claims.get(TokenUtils.ROLES);
				@SuppressWarnings("unchecked")
				List<String> grantedPermissions = (List<String>) claims.get(TokenUtils.PERMISSIONS_GRANTED);
				@SuppressWarnings("unchecked")
				List<String> revokedPermissions = (List<String>) claims.get(TokenUtils.PERMISSIONS_REVOKED);
				
				Set<String> permissions = new HashSet<>();
				permissions.addAll(roles);
				permissions.addAll(grantedPermissions);
				permissions.removeAll(revokedPermissions);
				
				Set<GrantedAuthority> auths = permissions
						.stream()
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toSet());
				
				UserDetails userDetail = new User(claims.getSubject(), "", auths);
			
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetail, null, auths);
				
		        SecurityContextHolder.getContext().setAuthentication(authentication);
		        
		        request.setAttribute("VALID_TOKEN", true);
		        
			} catch (Exception e) {

				request.setAttribute("VALID_TOKEN", false);
				request.setAttribute("TOKEN_ERROR", e.getLocalizedMessage());
			}
			
		}
		
		chain.doFilter(request, response);

	}

	private static String getTokenIfExists(HttpServletRequest request) {

		String authenticationHeader = request.getHeader(TokenUtils.HEADER);

		if (authenticationHeader != null && authenticationHeader.startsWith(TokenUtils.PREFIX)) {

			return authenticationHeader.replace(TokenUtils.PREFIX, "");
		}

		return null;
	}

}