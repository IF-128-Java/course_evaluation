package ita.softserve.course_evaluation.annotation;

import ita.softserve.course_evaluation.validator.StartDateBeforeEndDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartDateBeforeEndDateValidator.class)
public @interface StartDateBeforeEndDate {
	String message() default "End date should be greater than start date!";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	String startDateTime();
	String endDateTime();
	
}
