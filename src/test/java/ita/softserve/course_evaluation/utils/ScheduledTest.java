package ita.softserve.course_evaluation.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ScheduledTest {
	
	@Test
	void testCronValidSpecificExpression() {
		CronExpression expression = CronExpression.parse("0 0 12 * * *");
		LocalDateTime last = LocalDateTime.now();
		LocalDateTime expected = last.getHour()<12 ?
				                         last.withHour(12).withMinute(0).withSecond(0).withNano(0):
				                         last.plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0);
		
		LocalDateTime actual = expression.next(last);
		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(expected);

		last = actual;
		expected = expected.plusDays(0).plusHours(24);
		actual = expression.next(last);
		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(expected);

		last = actual;
		expected = expected.plusDays(0).plusHours(24);
		assertThat(actual).isNotNull();
		assertThat(expression.next(last)).isEqualTo(expected);
	}
	
}
