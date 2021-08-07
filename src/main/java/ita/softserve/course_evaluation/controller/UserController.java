package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
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

@Api(tags = "User service REST API")
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get All Users List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = UserDto.class),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 401, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> read() {
        final List<UserDto> users = userService.readAll();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Get User by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = UserDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> readById(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(userService.readById(id), HttpStatus.OK);
    }


    @ApiOperation(value = "Get User by Username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = UserDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> readByName(@RequestParam(name = "name") String name) {

        return new ResponseEntity<>(userService.readByFirstName(name), HttpStatus.OK);
    }

    @ApiOperation(value = "Create new User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = UserDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.createUser(userDto));
    }

    @ApiOperation(value = "Update User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable Long id) {
        userService.updateUser(updateUserDto, id);
    }

    @ApiOperation("Update user password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
	@PatchMapping("/{id}/update-password")
	@ResponseStatus(HttpStatus.OK)
	public void updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto, @PathVariable Long id){
		userService.updatePassword(updatePasswordDto, id);
	}

    @ApiOperation("Delete User by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(value = "id") long id) {
        userService.deleteUser(id);
    }

}