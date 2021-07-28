package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public ResponseEntity<List<UserDto>> read() {
		final List<UserDto> users = userService.readAll();
		
		return users != null && !users.isEmpty()
				       ? new ResponseEntity<>(users, HttpStatus.OK)
				       : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> readById(@PathVariable(value="id") long id){
		return new ResponseEntity<>(userService.readById(id),  HttpStatus.OK);
	}

	@GetMapping("/{firstName}")
	public ResponseEntity<UserDto> readByName(@PathVariable(value="firstName") String name){
		return new ResponseEntity<>(userService.readByFirstName(name), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.createUser(userDto));


	}

    @PutMapping
	public ResponseEntity<UserDto> updateUser (@RequestBody UserDto userDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.updateUser(userDto));

	}

    @DeleteMapping("/{id}")
	public void deleteUser(@PathVariable(value = "id") long id){
		userService.deleteUser(id);
	}

}