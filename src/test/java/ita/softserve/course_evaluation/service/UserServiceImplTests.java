package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserProfileDtoResponse;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.InvalidOldPasswordException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private static User user;

    @BeforeAll
    public static void beforeAll(){
        user = new User();
        user.setId(1L);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setEmail("email@mail.com");
        user.setPassword("password");
    }

    @AfterEach
    public void beforeEach(){
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    public void testReadById(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserProfileDtoResponse actual = userService.readUserProfileDtoResponseById(anyLong());

        assertEquals(user.getEmail(), actual.getEmail());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testReadByFirstName(){
        when(userRepository.findUserByFirstName(anyString())).thenReturn(List.of(user));

        List<UserDto> actual = userService.readByFirstName(anyString());

        assertFalse(actual.isEmpty());
        verify(userRepository, times(1)).findUserByFirstName(anyString());
    }

    @Test
    public void testUpdate(){
        UpdateUserDto dto = new UpdateUserDto();
        dto.setFirstName("NewFirstName");
        dto.setLastName("NewLastName");

        when(userRepository.findUserByEmail(StringUtils.EMPTY)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(any(User.class));

        userService.updateUser(dto, StringUtils.EMPTY);

        verify(userRepository, times(1)).findUserByEmail(StringUtils.EMPTY);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdatePasswordIfOldPasswordValid(){
        UpdatePasswordDto dto = new UpdatePasswordDto();
        dto.setOldPassword("OldPassword");
        dto.setNewPassword("NewPassword");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());
        lenient().when(userRepository.save(user)).thenReturn(user);

        userService.updatePassword(dto, anyString());

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdatePasswordIfOldPasswordInValid(){
        UpdatePasswordDto dto = new UpdatePasswordDto();
        dto.setOldPassword("OldPassword");
        dto.setNewPassword("NewPassword");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Exception exception = assertThrows(InvalidOldPasswordException.class,
                () -> userService.updatePassword(dto, anyString()));

        assertEquals("Old password doesn't match!", exception.getMessage());

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }
}
