package ita.softserve.course_evaluation.validator;

import ita.softserve.course_evaluation.annotation.StartDateBeforeEndDate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

@Slf4j
@Component
public class StartDateBeforeEndDateValidator implements ConstraintValidator<StartDateBeforeEndDate, Object> {
	
	private String startDateTime;
	private String endDateTime;
	
	@Override
	public void initialize(StartDateBeforeEndDate constraintAnnotation) {
		startDateTime = constraintAnnotation.startDateTime();
		endDateTime = constraintAnnotation.endDateTime();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
			final LocalDateTime currentBeginDateTime = LocalDateTime.parse(BeanUtils.getProperty(value, startDateTime));
			final LocalDateTime currentFinishTime = LocalDateTime.parse(BeanUtils.getProperty(value, endDateTime));
			return currentBeginDateTime.isBefore(currentFinishTime);
			
		} catch (final Exception e) {
			log.info("Exception was found during data validation", e);
		}
		return true;
	}
}
