package ita.softserve.course_evaluation.validator;

import ita.softserve.course_evaluation.annotation.ImageValidation;
import ita.softserve.course_evaluation.constants.ValidationConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
public class ImageValidator implements ConstraintValidator<ImageValidation, MultipartFile> {

    @Value("${max-image-size-in-mb}")
    private double maxImageSizeInMb;

    private final List<String> validType = List.of("image/jpeg", "image/png", "image/jpg");

    @Override
    public void initialize(ImageValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {
        if(image == null || image.getContentType() == null){
            addConstraintViolation(context, ValidationConstants.NULL_IMAGE);

            return false;
        }

        if(Double.compare(image.getSize() * 0.00000095367432, maxImageSizeInMb) > 0){
            addConstraintViolation(context, String.format(ValidationConstants.INVALID_IMAGE_SIZE, maxImageSizeInMb));

            return false;
        }

        return validType.contains(image.getContentType());
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}