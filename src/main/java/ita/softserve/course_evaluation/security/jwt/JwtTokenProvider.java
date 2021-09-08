package ita.softserve.course_evaluation.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.exception.JwtAuthenticationException;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {
	
	private final UserDetailsService userDetailsService;
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.header}")
	private String authorizationHeader;
	@Value("${jwt.expiration}")
	private long validity;
	private static final String AUTHENTICATED = "authenticated";
	public static final long TEMP_VALIDITY_IN_MILLIS = 300000;
	
	public JwtTokenProvider(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String createToken(String username, Long userId, String[] role, boolean authenticated) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("role", role);
		claims.put("id", userId);
		claims.put(AUTHENTICATED, authenticated);
		Date now = new Date();
		Date validity = new Date(now.getTime() + this.validity * 1000);
		Date expireDate = authenticated ? validity : new Date(now.getTime() + TEMP_VALIDITY_IN_MILLIS);
		return Jwts.builder()
				       .setClaims(claims)
				       .setIssuedAt(now)
				       .setExpiration(expireDate)
				       .signWith(SignatureAlgorithm.HS256, secretKey)
				       .compact();
	}

	public String createToken(Authentication authentication) {
		LocalUser userPrincipal = (LocalUser) authentication.getPrincipal();
		log.info("Token with Authentication parameter was created");
		return createToken(userPrincipal.getUser().getEmail()
				,userPrincipal.getUser().getId()
				, userPrincipal.getUser().getRoles().stream().map(Enum::name).toArray(String[]::new), true);
	}

	public Boolean isAuthenticated(String token){
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		return claims.get(AUTHENTICATED, Boolean.class);
	}
	
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return !claimsJws.getBody().getExpiration().before(new Date());
		} catch (JwtException | IllegalArgumentException e) {
			throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
		}
	}
	
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));

		Collection<? extends GrantedAuthority> authorities = isAuthenticated(token)
				? userDetails.getAuthorities()
				: List.of(new SimpleGrantedAuthority(Role.ROLE_PRE_VERIFICATION.name()));

		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}
	
	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}
	
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader(authorizationHeader);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return bearerToken;
	}
}
