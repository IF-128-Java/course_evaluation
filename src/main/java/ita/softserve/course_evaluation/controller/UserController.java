package ita.softserve.course_evaluation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ita.softserve.course_evaluation.constants.HttpStatuses;
import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.security.oauth2.LocalUser;
import ita.softserve.course_evaluation.security.SecurityUser;
import ita.softserve.course_evaluation.service.UserService;
import ita.softserve.course_evaluation.util.GeneralUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@Api(tags = "User service REST API")
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get User by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = UserDto.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<UserDto> readById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.readById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get User by Username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK, response = List.class),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> readByName(@RequestParam(name = "name") String name) {
        return new ResponseEntity<>(userService.readByFirstName(name), HttpStatus.OK);
    }

    @ApiOperation(value = "Update User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@Validated @RequestBody UpdateUserDto updateUserDto,
                           @ApiIgnore Principal principal) {
        userService.updateUser(updateUserDto, principal.getName());
    }

    @ApiOperation("Update user password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
	@PatchMapping("/update-password")
	@ResponseStatus(HttpStatus.OK)
	public void updatePassword(@Validated @RequestBody UpdatePasswordDto updatePasswordDto,
                               @ApiIgnore Principal principal){
		userService.updatePassword(updatePasswordDto, principal.getName());
	}

}