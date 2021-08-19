package ita.softserve.course_evaluation.validator;

import ita.softserve.course_evaluation.annotation.ImageValidation;
import ita.softserve.course_evaluation.constants.ValidationConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
public class ImageValidator implements ConstraintValidator<ImageValidation, MultipartFile> {
    private final List<String> validType = List.of("image/jpeg", "image/png", "image/jpg");

    @Override
    public void initialize(ImageValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
        if(image == null || image.getContentType() == null){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ValidationConstants.NULL_IMAGE)
                    .addConstraintViolation();

            return false;
        }

        return validType.contains(image.getContentType());
    }
}