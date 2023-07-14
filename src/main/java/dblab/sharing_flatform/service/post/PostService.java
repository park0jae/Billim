package dblab.sharing_flatform.service.post;

import dblab.sharing_flatform.domain.image.PostImage;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.item.crud.create.ItemCreateRequestDto;
import dblab.sharing_flatform.dto.post.crud.create.PostCreateRequestDto;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import dblab.sharing_flatform.dto.post.crud.read.response.PagedPostListDto;
import dblab.sharing_flatform.dto.post.crud.read.response.PostReadResponseDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateRequestDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateResponseDto;
import dblab.sharing_flatform.dto.image.postImage.PostImageDto;
import dblab.sharing_flatform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_flatform.exception.category.CategoryNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.item.ItemRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.service.file.PostFileService;
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
    private final PostFileService postFileService;

    public PagedPostListDto readAll(PostPagingCondition cond) {
        return PagedPostListDto.toDto(postRepository.findAllBySearch(cond));
    }

    public PostReadResponseDto read(Long id) {
        return PostReadResponseDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    // create
    @Transactional
    public void create(PostCreateRequestDto postCreateRequestDto) {
        List<PostImage> postImages = getImages(postCreateRequestDto);

        postRepository.save(new Post(postCreateRequestDto.getTitle(),
                postCreateRequestDto.getContent(),
                categoryRepository.findByName(postCreateRequestDto.getCategoryName()).orElseThrow(CategoryNotFoundException::new),
                postCreateRequestDto.getItemCreateRequestDto() != null ? itemRepository.save(ItemCreateRequestDto.toEntity(postCreateRequestDto.getItemCreateRequestDto())) : null,
                postImages,
                memberRepository.findByUsername(postCreateRequestDto.getUsername()).orElseThrow(AuthenticationEntryPointException::new),
                null));
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
        deleteImagesFromServer(post);
    }

    @Transactional
    public PostUpdateResponseDto update(Long id, PostUpdateRequestDto postUpdateRequestDto) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        PostUpdateResponseDto postUpdateResponseDto = post.updatePost(postUpdateRequestDto);

        updateImagesToServer(postUpdateRequestDto, postUpdateResponseDto);

        return postUpdateResponseDto;
    }

    // create
    public void uploadImagesToServer(List<PostImage> postImages, List<MultipartFile> fileImages) {
        if (!postImages.isEmpty()) {
            for (int i = 0; i < postImages.size(); i++) {
                postFileService.upload(fileImages.get(i), postImages.get(i).getUniqueName());
            }
        }
    }

    // delete
    public void deleteImagesFromServer(Post post) {
        List<PostImage> postImages = post.getPostImages();
        postImages.stream().forEach(i -> postFileService.delete(i.getUniqueName()));
    }

    // update
    public void updateImagesToServer(PostUpdateRequestDto postUpdateRequestDto, PostUpdateResponseDto postUpdateResponseDto) {
        uploadImagesToServer(postUpdateRequestDto, postUpdateResponseDto);
        deleteImagesFromServer(postUpdateResponseDto);
    }

    // update - create
    private void uploadImagesToServer(PostUpdateRequestDto postUpdateRequestDto, PostUpdateResponseDto postUpdateResponseDto) {
        List<PostImageDto> addedImages = postUpdateResponseDto.getAddedImages();
        for (int i = 0; i < addedImages.size(); i++) {
            postFileService.upload(postUpdateRequestDto.getAddImages().get(i), addedImages.get(i).getUniqueName());
        }
    }

    // update - delete
    private void deleteImagesFromServer(PostUpdateResponseDto postUpdateResponseDto) {
        List<PostImageDto> deletePostImageDtoList = postUpdateResponseDto.getDeletedImages();
        deletePostImageDtoList.stream().forEach(i -> postFileService.delete(i.getUniqueName()));
    }

    private List<PostImage> getImages(PostCreateRequestDto postCreateRequestDto) {
        List<PostImage> postImages;
        if (postCreateRequestDto.getMultipartFiles() != null) {
            postImages = MultiPartFileToImage(postCreateRequestDto.getMultipartFiles());
            uploadImagesToServer(postImages, postCreateRequestDto.getMultipartFiles());
        } else {
            postImages = List.of();
        }
        return postImages;
    }

    private List<PostImage> MultiPartFileToImage(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(i -> new PostImage(i.getOriginalFilename())).collect(Collectors.toList());
    }
}
