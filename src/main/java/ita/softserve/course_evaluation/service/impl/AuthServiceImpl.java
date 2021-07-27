package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.dto.AuthenticateRequestDto;
import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.dto.SimpleUserDtoMapper;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.security.jwt.JwtTokenProvider;
import ita.softserve.course_evaluation.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private JwtTokenProvider jwtTokenProvider;
	
	public AuthServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	public ResponseEntity<?> getLoginCredentials(AuthenticateRequestDto request) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
			String [] roles = user.getRoles().stream().map(Enum::name).toArray(String[]::new);
			String token = jwtTokenProvider.createToken(request.getEmail(), roles);
			Map<Object, Object> response = new HashMap<>();
			response.put("token", token);
			return ResponseEntity.ok(response);
		} catch (AuthenticationException e) {
			return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
		}
	}
	
	@Override
	public ResponseEntity<?> getRegistrationCredentials(SimpleUserDto dto) {
		Optional<User> userInDb = userRepository.findUserByEmail(dto.getEmail());
		if(userInDb.isEmpty()){
			User user = new User();
			user.setEmail(dto.getEmail());
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
			user.setFirstName(dto.getFirstName());
			user.setLastName(dto.getLastName());
			userRepository.save(user);
			return ResponseEntity.ok(SimpleUserDtoMapper.toDto(user));
		}
		return new ResponseEntity<>("User exist", HttpStatus.BAD_REQUEST);
	}
}