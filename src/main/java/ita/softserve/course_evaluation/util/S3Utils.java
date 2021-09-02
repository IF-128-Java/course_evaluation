package ita.softserve.course_evaluation.util;

import ita.softserve.course_evaluation.exception.FileHasNoExtensionException;
import ita.softserve.course_evaluation.exception.FileProcessingException;
import ita.softserve.course_evaluation.service.s3.AmazonS3ClientService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class S3Utils {
    private final AmazonS3ClientService s3ClientService;

    public S3Utils(AmazonS3ClientService s3ClientService) {
        this.s3ClientService = s3ClientService;
    }

    public String saveFile(MultipartFile file, String bucketName, String folderName){
        String extension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String fileReference = UUID.randomUUID().toString().toLowerCase() + "." + extension;

        try {
            s3ClientService.upload(bucketName, folderName + "/" + fileReference, file.getInputStream());
        }catch (IOException e){
            throw new FileProcessingException("File has not been saved!");
        }

        return fileReference;
    }

    public void deleteFile(String fileReference, String bucketName, String folderName){
        s3ClientService.delete(bucketName, folderName + "/" + fileReference);
    }

    public byte[] downloadFile(String fileReference, String bucketName, String folderName){
        byte[] bytes;

        try {
            bytes = s3ClientService.download(bucketName, folderName + "/" + fileReference);
        }catch (IOException e) {
            throw new FileProcessingException("File has not been downloaded!");
        }

        return bytes;
    }

    private String getFileExtension(String fileName){
        String[] split = fileName.split("\\.");
        if(split.length < 1)
            throw new FileHasNoExtensionException("Wrong file format!");

        return split[split.length - 1];
    }
}
