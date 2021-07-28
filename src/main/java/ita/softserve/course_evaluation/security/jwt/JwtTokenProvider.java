package ita.softserve.course_evaluation.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ita.softserve.course_evaluation.exception.JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
	
	private final UserDetailsService userDetailsService;
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.header}")
	private String authorizationHeader;
	@Value("${jwt.expiration}")
	private long validity;
	
	public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	public String createToken(String username, String[] role) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("role", role);
		Date now = new Date();
		Date validity = new Date(now.getTime() + this.validity * 1000);
		return Jwts.builder()
				       .setClaims(claims)
				       .setIssuedAt(now)
				       .setExpiration(validity)
				       .signWith(SignatureAlgorithm.HS256, secretKey)
				       .compact();
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
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}
	
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader(authorizationHeader);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return req.getHeader(authorizationHeader);
	}
}
