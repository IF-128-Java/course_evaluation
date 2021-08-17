package ita.softserve.course_evaluation.registration;

import ita.softserve.course_evaluation.dto.SimpleUserDto;
import ita.softserve.course_evaluation.entity.User;
import ita.softserve.course_evaluation.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final UserService userService;



    public String register(SimpleUserDto request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userService.signUp(user);
    }
}
