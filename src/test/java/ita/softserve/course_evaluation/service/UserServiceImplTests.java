package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.dto.UpdatePasswordDto;
import ita.softserve.course_evaluation.dto.UpdateUserDto;
import ita.softserve.course_evaluation.dto.UserDto;
import ita.softserve.course_evaluation.dto.UserProfileDtoResponse;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.exception.InvalidOldPasswordException;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.impl.AmazonS3FileManager;
import ita.softserve.course_evaluation.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
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

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AmazonS3FileManager fileManager;

    @InjectMocks
    private UserServiceImpl userService;

    private User expectedUser;

    @BeforeEach
    public void beforeEach(){
        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setFirstName("First Name");
        expectedUser.setLastName("Last Name");
        expectedUser.setEmail("email@mail.com");
        expectedUser.setPassword("password");
        expectedUser.setProfilePicturePath("default");
    }

    @AfterEach
    public void afterEach(){
        verifyNoMoreInteractions(userRepository, passwordEncoder, fileManager);
    }

    @Test
    public void testReadUserProfileDtoResponseById(){
        byte[] pictureByte = new byte[]{1,2,3};
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(fileManager.downloadUserProfilePicture(anyString())).thenReturn(pictureByte);

        UserProfileDtoResponse actual = userService.readUserProfileDtoResponseById(anyLong());

        assertEquals(expectedUser.getEmail(), actual.getEmail());
        verify(userRepository, times(1)).findById(anyLong());
        verify(fileManager, times(1)).downloadUserProfilePicture(anyString());
    }

    @Test
    public void testReadUserById(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));

        User actual = userService.readUserById(anyLong());

        assertEquals(expectedUser, actual);
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testReadByFirstName(){
        when(userRepository.findUserByFirstName(anyString())).thenReturn(List.of(expectedUser));

        List<UserDto> actual = userService.readByFirstName(anyString());

        assertFalse(actual.isEmpty());
        verify(userRepository, times(1)).findUserByFirstName(anyString());
    }

    @Test
    public void testUpdateUser(){
        UpdateUserDto dto = new UpdateUserDto();
        dto.setFirstName("NewFirstName");
        dto.setLastName("NewLastName");

        when(userRepository.findUserByEmail(StringUtils.EMPTY)).thenReturn(Optional.of(expectedUser));
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

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expectedUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn(anyString());
        lenient().when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        userService.updatePassword(dto, anyString());

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(expectedUser);
    }

    @Test
    public void testUpdatePasswordIfOldPasswordInvalid(){
        UpdatePasswordDto dto = new UpdatePasswordDto();
        dto.setOldPassword("OldPassword");
        dto.setNewPassword("NewPassword");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expectedUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Exception exception = assertThrows(InvalidOldPasswordException.class,
                () -> userService.updatePassword(dto, anyString()));

        assertEquals("Old password doesn't match!", exception.getMessage());

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testUpdateUserProfilePictureIfOldPictureExists(){
        MultipartFile multipartFile = new MockMultipartFile("fileName", new byte[]{1, 2, 3});

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expectedUser));
        when(fileManager.uploadUserProfilePicture(multipartFile)).thenReturn(anyString());
        lenient().when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        userService.updateUserProfilePicture(multipartFile, anyString());

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(fileManager, times(1)).deleteUserProfilePicture(anyString());
        verify(fileManager, times(1)).uploadUserProfilePicture(multipartFile);
        verify(userRepository, times(1)).save(expectedUser);
    }

    @Test
    public void testUpdateUserProfilePictureIfOldPictureNotExist(){
        MultipartFile multipartFile = new MockMultipartFile("fileName", new byte[]{1, 2, 3});
        expectedUser.setProfilePicturePath(null);

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expectedUser));
        when(fileManager.uploadUserProfilePicture(multipartFile)).thenReturn(anyString());
        lenient().when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        userService.updateUserProfilePicture(multipartFile, anyString());

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(fileManager, never()).deleteUserProfilePicture(anyString());
        verify(fileManager, times(1)).uploadUserProfilePicture(multipartFile);
        verify(userRepository, times(1)).save(expectedUser);
    }

    @Test
    public void testDeleteUserProfilePictureIfExists(){
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expectedUser));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        userService.deleteUserProfilePicture(anyString());

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(fileManager, times(1)).deleteUserProfilePicture(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteUserProfilePictureIfNotExist(){
        expectedUser.setProfilePicturePath(null);

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expectedUser));

        userService.deleteUserProfilePicture(anyString());

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(fileManager,never()).deleteUserProfilePicture(anyString());
        verify(userRepository, never()).save(any());
    }
}