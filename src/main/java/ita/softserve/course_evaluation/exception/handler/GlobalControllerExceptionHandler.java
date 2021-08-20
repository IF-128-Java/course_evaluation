package ita.softserve.course_evaluation.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import ita.softserve.course_evaluation.exception.CourseAlreadyExistException;
import ita.softserve.course_evaluation.exception.CourseNotFoundException;
import ita.softserve.course_evaluation.exception.InvalidOldPasswordException;
import ita.softserve.course_evaluation.exception.JwtAuthenticationException;
import ita.softserve.course_evaluation.exception.dto.GenericExceptionResponse;
import ita.softserve.course_evaluation.exception.dto.ValidationExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CourseNotFoundException.class})
    public ResponseEntity<GenericExceptionResponse> handleCourseNotFoundException(CourseNotFoundException exception) {

        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CourseAlreadyExistException.class})
    public ResponseEntity<GenericExceptionResponse> handleCourseAlreadyExistException(CourseAlreadyExistException exception) {

        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JwtAuthenticationException.class})
    public ResponseEntity<GenericExceptionResponse> handleJwtAuthenticationException(JwtAuthenticationException exception) {

        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<GenericExceptionResponse> handleAuthException(AuthenticationException exception) {

        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<GenericExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {

        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JsonProcessingException.class})
    public ResponseEntity<GenericExceptionResponse> handleJsonProcessingException(JsonProcessingException exception) {

        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<GenericExceptionResponse> handleEntityNotFoundException(EntityNotFoundException exception) {

        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<GenericExceptionResponse> handleNotFoundException(UsernameNotFoundException exception) {

        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidOldPasswordException.class})
    public ResponseEntity<GenericExceptionResponse> handleInvalidOldPasswordException(InvalidOldPasswordException exception) {

        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<GenericExceptionResponse> onConstraintValidationException(ConstraintViolationException exception) {
        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(
                        exception.getConstraintViolations()
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining())
                )
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ValidationExceptionResponse dto = ValidationExceptionResponse.builder()
                .fields(
                        exception.getBindingResult()
                                .getAllErrors()
                                .stream()
                                .collect(Collectors.toMap(
                                        (error) -> {
                                            if (error instanceof FieldError) {
                                                return ((FieldError) error).getField();
                                            } else {
                                                return Objects.requireNonNull(error.getDefaultMessage()).substring(0,error.getDefaultMessage().indexOf(" "));
                                            }
                                        },
                                        DefaultMessageSourceResolvable::getDefaultMessage
                                ))
                )
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        log.info("Global Exception Handler invoke: {}", exception.getMessage());

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}