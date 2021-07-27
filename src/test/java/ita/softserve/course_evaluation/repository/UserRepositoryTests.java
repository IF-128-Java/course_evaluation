package ita.softserve.course_evaluation.repository;

import ita.softserve.course_evaluation.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserByFirstNameIfExists(){
        User user = new User();
        user.setFirstName("First Name");

        User expected = userRepository.save(user);
        Optional<User> actual = userRepository.findUserByFirstName(expected.getFirstName());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testFindUserByFirstNameIfNotExist(){
        Optional<User> actual = userRepository.findUserByFirstName("");

        assertFalse(actual.isPresent());
    }
}