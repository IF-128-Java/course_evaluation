package ita.softserve.course_evaluation.validator;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
        //TODO: Implement Regex email
        return true;
    }
}
