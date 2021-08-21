package ita.softserve.course_evaluation.annotation;

import ita.softserve.course_evaluation.constants.ValidationConstants;
import ita.softserve.course_evaluation.validator.ImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = ImageValidator.class)
public @interface ImageValidation {
    String message() default ValidationConstants.INVALID_IMAGE_TYPE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}