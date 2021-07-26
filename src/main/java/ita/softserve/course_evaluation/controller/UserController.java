package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping(value = "/users")
	public ResponseEntity<List<UserDto>> read() {
		final List<UserDto> users = userService.readAll();
		
		return users != null && !users.isEmpty()
				       ? new ResponseEntity<>(users, HttpStatus.OK)
				       : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(value="/user/{id}")
	public ResponseEntity<UserDto> readById(@PathVariable(value="id") long id){
		return new ResponseEntity<>(userService.readById(id),  HttpStatus.OK);
	}

	@GetMapping(value="user/{firstName}")
	public ResponseEntity<UserDto> readByName(@PathVariable(value="firstName") String name){
		return new ResponseEntity<>(userService.readByFirstName(name), HttpStatus.OK);
	}

	@PostMapping(value="/user")
	public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.createUser(userDto));


	}

    @PutMapping(value="/user")
	public ResponseEntity<UserDto> updateUser (@RequestBody UserDto userDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.updateUser(userDto));

	}

    @DeleteMapping(value="/user/{id}")
	public void deleteUser(@PathVariable(value = "id") long id){
		userService.deleteUser(id);
	}

}