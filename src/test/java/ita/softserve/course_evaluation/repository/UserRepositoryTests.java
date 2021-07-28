package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.Role;
import ita.softserve.course_evaluation.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    public static User user;

    @BeforeAll
    public static void afterAll(){
        user = new User();
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setEmail("email@mail.com");
        user.setPassword("password");
        user.setRoles(Set.of(Role.ROLE_STUDENT));
    }

    @Test
    public void testFindUserByFirstNameIfExists(){
        User expected = userRepository.save(user);
        Optional<User> actual = userRepository.findUserByFirstName(expected.getFirstName());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testFindUserByFirstNameIfNotExist(){
        Optional<User> actual = userRepository.findUserByFirstName(StringUtils.EMPTY);

        assertFalse(actual.isPresent());
    }

    @Test
    public void testFindUserByEmailIfExists(){
        User expected = userRepository.save(user);
        Optional<User> actual = userRepository.findUserByEmail(expected.getEmail());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testFindUserByEmailIfNotExist(){
        Optional<User> actual = userRepository.findUserByEmail(StringUtils.EMPTY);

        assertFalse(actual.isPresent());
    }
}