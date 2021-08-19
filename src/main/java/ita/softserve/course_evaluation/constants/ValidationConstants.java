package ita.softserve.course_evaluation.constants;

public final class ValidationConstants {
    public static final String INVALID_FIRST_NAME_PATTERN = "The first name must start with a capital letter followed by one to ten lowercase letters!";
    public static final String INVALID_LAST_NAME_PATTERN = "The last name must start with a capital letter followed by one to ten lowercase letters!";

    public static final String NULL_FIRST_NAME = "The first name can not be null!";
    public static final String NULL_LAST_NAME = "The last name can not be null!";

    public static final String USERNAME_PATTERN = "\\p{Lu}[\\p{Ll}']{1,10}";

    public static final String EMPTY_OLD_PASSWORD = "The old password can not be empty!";
    public static final String EMPTY_NEW_PASSWORD = "The new password can not be empty!";

    public static final String NULL_IMAGE = "Image can not be null!";
    public static final String INVALID_IMAGE_TYPE = "Download PNG or JPEG only!";
}