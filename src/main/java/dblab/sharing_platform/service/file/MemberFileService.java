package dblab.sharing_platform.service.file;


import dblab.sharing_platform.exception.file.FileUploadFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class MemberFileService {

    private static final String UPLOAD_MEMBER_IMAGE_LOCATION = "${upload.member.image.location}";

    @Value(UPLOAD_MEMBER_IMAGE_LOCATION)
    private String location;

    @PostConstruct
    void postConstruct() {
        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void upload(MultipartFile file, String filename) {
        try {
            file.transferTo(new File(location + filename));
        } catch(IOException e) {
            throw new FileUploadFailureException();
        }
    }

    public void delete(String filename) {
        new File(location + filename).delete();
    }
}