package ita.softserve.course_evaluation.service.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AmazonS3ClientService {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    private AmazonS3 s3client;

    @PostConstruct
    public void connectClient(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    public void upload(String bucketName, String fileName, InputStream inputStream){
        s3client.putObject(bucketName, fileName, inputStream, new ObjectMetadata());
    }

    public byte[] download(String bucketName, String fileReference) throws IOException {
        return s3client.getObject(bucketName, fileReference).getObjectContent().readAllBytes();
    }

    public void delete(String bucketName, String fileReference){
        s3client.deleteObject(bucketName, fileReference);
    }
}
