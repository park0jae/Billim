package dblab.sharing_platform.helper;

import dblab.sharing_platform.domain.image.PostImage;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.dto.post.PostUpdateRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PostImageHelper {

    public static List<PostImage> addToDBAndServer(List<MultipartFile> addImages, List<PostImage> postImages, Post post) {
        List<PostImage> addPostImageList = MultipartToImage(addImages);
        addImages(addPostImageList, postImages, post);

        return addPostImageList;
    }

    public static List<PostImage> deleteFromDBAndServer(List<String> deleteImageNames, List<PostImage> postImages, Post post) {
        List<PostImage> deletePostImageList = StringToImage(deleteImageNames, postImages);
        deleteImages(deletePostImageList, postImages, post);

        return deletePostImageList;
    }

    public static void addImages(List<PostImage> addList, List<PostImage> postImages, Post post) {
        addList.stream().forEach(
                i -> {
                    postImages.add(i);
                    i.initPost(post);
                });
    }

    private static void deleteImages(List<PostImage> deleteList, List<PostImage> postImages, Post post) {
        deleteList.stream().forEach(
                i -> {
                    postImages.remove(i);
                    i.cancel(post);
                }
        );
    }

    public static Map<String, List<PostImage>> updateImage(PostUpdateRequestDto requestDto, List<PostImage> postImages, Post post) {

        Map<String, List<PostImage>> m = new HashMap<>();

        // 수정/DB - 추가된 이미지 데이터베이스에 올리고, 삭제된 이미지 데이터베이스에서 삭제
        if (requestDto.getAddImages() != null) {
            List<MultipartFile> addImages = requestDto.getAddImages();  // 업로드할 이미지 파일
            List<PostImage> addList = addToDBAndServer(addImages, postImages, post);
            m.put("addList", addList);
        }

        if (requestDto.getDeleteImageNames() != null) {
            List<String> deleteImageNames = requestDto.getDeleteImageNames(); // 삭제할 이미지 파일 이름
            List<PostImage> deleteList = deleteFromDBAndServer(deleteImageNames, postImages, post);
            m.put("deleteList", deleteList);
        }

        return m;
    }

    private static Optional<PostImage> convertNameToImage(String name, List<PostImage> postImages) {
        return postImages.stream().filter(i -> i.getOriginName().equals(name)).findAny();
    }

    private static List<PostImage> StringToImage(List<String> deleteImageNames, List<PostImage> postImages) {
        return deleteImageNames.stream().map(name -> convertNameToImage(name, postImages))
                .filter(i -> i.isPresent())
                .map(i -> i.get())
                .collect(Collectors.toList());
    }

    private static List<PostImage> MultipartToImage(List<MultipartFile> addImages) {
        return addImages.stream().map(
                file -> new PostImage(file.getOriginalFilename())).collect(Collectors.toList());
    }


}
