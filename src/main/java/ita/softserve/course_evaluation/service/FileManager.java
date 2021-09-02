package ita.softserve.course_evaluation.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {

    byte[] downloadUserProfilePicture(String profilePicturePath);

    String uploadUserProfilePicture(MultipartFile image);

    void deleteUserProfilePicture(String profilePicturePath);
}