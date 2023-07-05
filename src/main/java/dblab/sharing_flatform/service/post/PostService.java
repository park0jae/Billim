package dblab.sharing_flatform.service.post;

import dblab.sharing_flatform.domain.image.Image;
import dblab.sharing_flatform.domain.item.Item;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.item.ItemCreateRequestDto;
import dblab.sharing_flatform.dto.item.ItemUpdateRequestDto;
import dblab.sharing_flatform.dto.post.PostCreateRequestDto;
import dblab.sharing_flatform.dto.post.PostUpdateRequestDto;
import dblab.sharing_flatform.dto.post.PostUpdateResponseDto;
import dblab.sharing_flatform.dto.post.image.ImageDto;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_flatform.exception.category.CategoryNotFoundException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.item.ItemRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final FileService fileService;

    // create
    @Transactional
    public void create(PostCreateRequestDto postCreateRequestDto) {

        // 글에 올라갈 MultiPartFile -> Image 로 변환
        List<Image> images = MultiPartFileToImage(postCreateRequestDto.getMultipartFiles());

        // 글 저장
        postRepository.save(new Post(postCreateRequestDto.getTitle(),
                postCreateRequestDto.getContent(),
                categoryRepository.findByName(postCreateRequestDto.getCategoryName()).orElseThrow(CategoryNotFoundException::new),
                postCreateRequestDto.getItemCreateRequestDto() != null ? itemRepository.save(ItemCreateRequestDto.toEntity(postCreateRequestDto.getItemCreateRequestDto())) : null,
                images,
                memberRepository.findByUsername(postCreateRequestDto.getUsername()).orElseThrow(AuthenticationEntryPointException::new)));

        // 로컬 서버에 post에 첨부한 이미지 업로드
        uploadImagesToServer(images, postCreateRequestDto.getMultipartFiles());
    }

    private List<Image> MultiPartFileToImage(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(i -> new Image(i.getOriginalFilename())).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
        deleteImagesFromServer(post);
    }

    /**
     *     public void update(String title, String content, Category category, Item item, List<Image> images) {
     *         this.title = title;
     *         this.content = content;
     *         this.category = category;
     *         this.item = item;
     *         addImages(images);
    **/

    @Transactional
    public PostUpdateResponseDto update(Long id, PostUpdateRequestDto postUpdateRequestDto) {
        // 수정할 게시물 찾고
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        // 1. DB - Post 수정
        // 2. DB - Image 수정 - 추가 및 삭제
        PostUpdateResponseDto postUpdateResponseDto = post.updatePost(postUpdateRequestDto);

//        // 3. 로컬 서버 내 이미지 - 추가 및 삭제
        updateImagesToServer(postUpdateRequestDto, postUpdateResponseDto);

        return postUpdateResponseDto;
    }


    // create
    public void uploadImagesToServer(List<Image> images, List<MultipartFile> fileImages) {
        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                fileService.upload(fileImages.get(i), images.get(i).getUniqueName());
            }
        }
    }

    // delete
    public void deleteImagesFromServer(Post post) {
        List<Image> images = post.getImages();
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }

    // update
    public void updateImagesToServer(PostUpdateRequestDto postUpdateRequestDto, PostUpdateResponseDto postUpdateResponseDto) {
        uploadImagesToServer(postUpdateRequestDto, postUpdateResponseDto);
        deleteImagesFromServer(postUpdateResponseDto);
    }

    // update - create
    private void uploadImagesToServer(PostUpdateRequestDto postUpdateRequestDto, PostUpdateResponseDto postUpdateResponseDto) {
        List<ImageDto> addedImages = postUpdateResponseDto.getAddedImages();
        for (int i = 0; i < addedImages.size(); i++) {
            fileService.upload(postUpdateRequestDto.getAddImages().get(i), addedImages.get(i).getUniqueName());
        }
    }

    // update - delete
    private void deleteImagesFromServer(PostUpdateResponseDto postUpdateResponseDto) {
        List<ImageDto> deleteImageDtoList = postUpdateResponseDto.getDeletedImages();
        deleteImageDtoList.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }
}
