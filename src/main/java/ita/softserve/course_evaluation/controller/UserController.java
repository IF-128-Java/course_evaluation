package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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


	@GetMapping("/search")
	public ResponseEntity<List<UserDto>> readByName(@RequestParam(name="name") String name){

		return new ResponseEntity<>(userService.readByFirstName(name), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.createUser(userDto));


	}

    @PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) {
		userService.updateUser(updateUserDto, id);
	}

	@PatchMapping("/{id}/update-password")
	@ResponseStatus(HttpStatus.OK)
	public void updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto, @PathVariable Long id){
		userService.updatePassword(updatePasswordDto, id);
	}

    @DeleteMapping("/{id}")
	public void deleteUser(@PathVariable(value = "id") long id){
		userService.deleteUser(id);
	}

}