package ita.softserve.course_evaluation.service;

import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.repository.UserRepository;
import ita.softserve.course_evaluation.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTests {
	
	private User expectedUser;
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private NotificationServiceImpl notificationService;
	
	@BeforeEach
	public void beforeEach() {
		expectedUser = new User();
		expectedUser.setId(1L);
		expectedUser.setFirstName("First Name");
		expectedUser.setLastName("Last Name");
		expectedUser.setEmail("email@mail.com");
		expectedUser.setPassword("password");
		expectedUser.setProfilePicturePath("default");
		
		
	}
	
	@Test
	void testSendNotificationToNotExistUser() {
		when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class, () -> notificationService.sendNotificationToUser(anyString(), 1L));
		verifyNoMoreInteractions(userRepository);
	}
	
}