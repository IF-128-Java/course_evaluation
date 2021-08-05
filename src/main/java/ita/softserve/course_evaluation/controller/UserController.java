package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Api(tags = "User service REST API")
@RestController
@RequestMapping("api/v1/users")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@ApiOperation(value = "Get All Users List")
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> read() {
		final List<UserDto> users = userService.readAll();
		
		return users != null && !users.isEmpty()
				       ? new ResponseEntity<>(users, HttpStatus.OK)
				       : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@ApiOperation(value = "Get User by ID")
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> readById(@PathVariable(value="id") long id){
		return new ResponseEntity<>(userService.readById(id),  HttpStatus.OK);
	}

	@ApiOperation(value = "Get User by Username")
	@GetMapping
	public ResponseEntity<UserDto> readByName(@RequestParam String name){

		return new ResponseEntity<>(userService.readByFirstName(name), HttpStatus.OK);
	}

	@ApiOperation(value = "Create new User")
	@PostMapping
	public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.createUser(userDto));
	}

	@ApiOperation(value = "Update User")
    @PutMapping
	public ResponseEntity<UserDto> updateUser (@RequestBody UserDto userDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.updateUser(userDto));

	}

	@ApiOperation("Delete User by Id")
    @DeleteMapping("/{id}")
	public void deleteUser(@PathVariable(value = "id") long id){
		userService.deleteUser(id);
	}

}