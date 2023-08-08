package dblab.sharing_platform.domain.image;

import dblab.sharing_platform.exception.image.NoExtException;
import dblab.sharing_platform.exception.image.UnSupportExtException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {

    private static final String extension[] = {"jpg", "jpeg", "gif", "bmp", "png"};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_image_id")
    private Long id;

    @Column(nullable = false)
    private String uniqueName;

    @Column(nullable = false)
    private String originName;

    public ProfileImage(String originName) {
        this.originName = originName;
        this.uniqueName = extractExtAndGenerateUniqueName(originName);
    }

    private String extractExtAndGenerateUniqueName(String originName) {
        String ext = getExt(originName);
        return UUID.randomUUID().toString() + "." + ext;
    }

    private String getExt(String originName) {
        try {
            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            if (supportFormat(ext)) {
                return ext;
            } else {
                throw new UnSupportExtException();
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new NoExtException();
        } catch (UnSupportExtException e) {
            throw e;
        }
    }

    private boolean supportFormat(String ext) {
        return Arrays.stream(extension).anyMatch(e -> e.equalsIgnoreCase(ext));
    }
}
