package dblab.sharing_platform.service.file;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import dblab.sharing_platform.exception.file.FileUploadFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PostFileService {

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;

    private final AmazonS3Client amazonS3Client;

    private final String location = "http://" + BUCKET;

    @PostConstruct
    void postConstruct() {
        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void upload(MultipartFile file, String filename) {
        try {
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(BUCKET, filename, file.getInputStream(), metadata);
        } catch(IOException e) {
            throw new FileUploadFailureException();
        }
    }

    public void delete(String fileName){
        DeleteObjectRequest request = new DeleteObjectRequest(BUCKET, fileName);
        amazonS3Client.deleteObject(request);
    }
}