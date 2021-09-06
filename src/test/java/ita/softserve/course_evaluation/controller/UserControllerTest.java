package ita.softserve.course_evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ita.softserve.course_evaluation.config.SpringSecurityTestConfiguration;
import ita.softserve.course_evaluation.config.WithMockCustomUser;
import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserDtoMapper;
import ita.softserve.course_evaluation.dto.UserProfileDtoResponse;
import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.handler.GlobalControllerExceptionHandler;
import ita.softserve.course_evaluation.service.UserService;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Arsen Kushnir on 03.09.2021
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserController.class, SpringSecurityTestConfiguration.class, GlobalControllerExceptionHandler.class})
public class UserControllerTest {
    public static final String API_USERS_URL = "/api/v1/users";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private static UserDto userDto;
    private static UserProfileDtoResponse userProfileDtoResponse;

    private final ObjectMapper mapper = new ObjectMapper();
    private final JSONParser jsonParser= new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

    @BeforeAll
    public static void beforeAll(){
        User user = new User();
        user.setId(1L);
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("email@mail.com");
        user.setRoles(Set.of(Role.ROLE_STUDENT, Role.ROLE_TEACHER));

        userDto = UserDtoMapper.toDto(user);

        userProfileDtoResponse = UserProfileDtoResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(new byte[]{1, 2, 3})
                .build();
    }

    @AfterEach
    void afterEach(){
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockCustomUser()
    void testReadByIdIfHaveAccess() throws Exception {
        when(userService.readUserProfileDtoResponseById(anyLong())).thenReturn(userProfileDtoResponse);

        String actual = mvc.perform(
                get(API_USERS_URL + "/1/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.firstName").value(userProfileDtoResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userProfileDtoResponse.getLastName()))
                .andExpect(jsonPath("$.email").value(userProfileDtoResponse.getEmail()))
                .andExpect(jsonPath("$.profilePicture").value(
                        jsonParser.parse(mapper.writeValueAsString(userProfileDtoResponse.getProfilePicture())))
                )
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(userProfileDtoResponse);

        assertEquals(expected, actual);
        verify(userService, times(1)).readUserProfileDtoResponseById(anyLong());
    }

    @Test
    @WithMockCustomUser(id = 2)
    void testReadByIdIfNotHaveAccess() throws Exception {
        mvc.perform(
                get(API_USERS_URL + "/1/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService, never()).readUserProfileDtoResponseById(anyLong());
    }

    @Test
    void testReadByName() throws Exception {
        when(userService.readByFirstName(anyString())).thenReturn(List.of(userDto));

        String actual = mvc.perform(
                        get(API_USERS_URL + "/search")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("name", userDto.getFirstName()))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(userDto.getLastName()))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()))
                .andExpect(jsonPath("$[0].roles").value(
                        containsInAnyOrder(((JSONArray)jsonParser.parse(mapper.writeValueAsString(userDto.getRoles()))).toArray()))
                )
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        String expected = mapper.writeValueAsString(List.of(userDto));

        assertEquals(expected, actual);
        verify(userService, times(1)).readByFirstName(anyString());
    }

    @Test
    @WithMockCustomUser(email = "email@mail.com")
    void testUpdateUser() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("Name");
        updateUserDto.setLastName("Name");

        mvc.perform(patch(API_USERS_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(updateUserDto, "email@mail.com");
    }

    @Test
    @WithMockCustomUser(email = "email@mail.com")
    void testUpdatePassword() throws Exception {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setOldPassword("1111");
        updatePasswordDto.setNewPassword("2222");

        mvc.perform(patch(API_USERS_URL + "/update-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatePasswordDto)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updatePassword(updatePasswordDto, "email@mail.com");
    }

    @Test
    @WithMockCustomUser(email = "email@mail.com")
    void testUpdateUserProfilePicture() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("image", "originFileName", MediaType.IMAGE_JPEG_VALUE, new byte[]{1, 2, 3});

        mvc.perform(multipart(API_USERS_URL + "/profile-picture")
                .file(multipartFile)
                .with(
                        request -> {
                            request.setMethod("PATCH");
                            return request;
                        }
                )
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserProfilePicture(multipartFile, "email@mail.com");
    }

    @Test
    @WithMockCustomUser(email = "email@mail.com")
    void testDeleteUserProfilePicture() throws Exception {

        mvc.perform(delete(API_USERS_URL + "/profile-picture"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserProfilePicture("email@mail.com");
    }
}