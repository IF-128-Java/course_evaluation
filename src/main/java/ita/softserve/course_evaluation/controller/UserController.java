package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping(value = "/users")
	public ResponseEntity<List<User>> read() {
		final List<User> users = userService.readAll();
		
		return users != null && !users.isEmpty()
				       ? new ResponseEntity<>(users, HttpStatus.OK)
				       : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(value="/user/{id}")
	public ResponseEntity<User> readById(@PathVariable(value="id") long id){
		return new ResponseEntity<>(userService.readById(id).get(),  HttpStatus.OK);
	}

	@GetMapping(value="user/{firstName}")
	public ResponseEntity<User> readByName(@PathVariable(value="firstName") String name){
		return new ResponseEntity<>(userService.readByFirstName(name).get(), HttpStatus.OK);
	}

	@PostMapping(value="/user")
	public void addUser(@RequestBody User user) {
		userService.createUser(user);

	}

    @PutMapping(value="/user")
	public void updateUser (@RequestBody User user) {
		userService.updateUser(user);
	}

    @DeleteMapping(value="/user/{id}")
	public void deleteUser(@PathVariable(value = "id") long id){
		userService.deleteUser(id);
	}

}