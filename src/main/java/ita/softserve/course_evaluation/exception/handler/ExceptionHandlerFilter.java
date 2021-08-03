package ita.softserve.course_evaluation.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ita.softserve.course_evaluation.exception.dto.GenericExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Exception Handler Filter invoke");
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {

            GenericExceptionResponse errorResponse = GenericExceptionResponse.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.FORBIDDEN.value())
                    .error(e.getClass().getSimpleName())
                    .build();

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            mapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}