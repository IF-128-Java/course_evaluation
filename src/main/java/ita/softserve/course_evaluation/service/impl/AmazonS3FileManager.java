package ita.softserve.course_evaluation.service.impl;

import ita.softserve.course_evaluation.service.FileManager;
import ita.softserve.course_evaluation.util.S3Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Objects;

@Service
public class AmazonS3FileManager implements FileManager {

    @Value("${aws.users-folder}")
    private String USERS_FOLDER;

    @Value("${aws.bucket-name}")
    private String BUCKET_NAME;

    private final S3Utils s3Utils;

    public AmazonS3FileManager(S3Utils s3Utils) {
        this.s3Utils = s3Utils;
    }

    @Override
    public byte[] downloadUserProfilePicture(String profilePicturePath){
        return Objects.isNull(profilePicturePath) ? null : s3Utils.downloadFile(profilePicturePath, BUCKET_NAME, USERS_FOLDER);
    }

    @Override
    public String uploadUserProfilePicture(MultipartFile image){
        return s3Utils.saveFile(image, BUCKET_NAME, USERS_FOLDER);
    }

    @Override
    public void deleteUserProfilePicture(String profilePicturePath){
        s3Utils.deleteFile(profilePicturePath, BUCKET_NAME, USERS_FOLDER);
    }
}