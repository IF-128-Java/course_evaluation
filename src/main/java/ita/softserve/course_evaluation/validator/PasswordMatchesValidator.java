package ita.softserve.course_evaluation.validator;

import ita.softserve.course_evaluation.dto.SimpleUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, SimpleUserDto> {

    @Override
    public boolean isValid(final SimpleUserDto user, final ConstraintValidatorContext context) {
        return user.getPassword().equals(user.getPasswordMatching());
    }

}
