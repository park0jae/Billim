package dblab.sharing_flatform.service.post;

import dblab.sharing_flatform.domain.image.PostImage;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.item.crud.create.ItemCreateRequestDto;
import dblab.sharing_flatform.dto.post.crud.create.PostCreateRequestDto;
import dblab.sharing_flatform.dto.post.crud.create.PostCreateResponseDto;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import dblab.sharing_flatform.dto.post.crud.read.response.PagedPostListDto;
import dblab.sharing_flatform.dto.post.crud.read.response.PostReadResponseDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateRequestDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateResponseDto;
import dblab.sharing_flatform.dto.image.postImage.PostImageDto;
import dblab.sharing_flatform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_flatform.exception.category.CategoryNotFoundException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.repository.category.CategoryRepository;
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
    private final PostFileService postFileService;

    public PagedPostListDto readAll(PostPagingCondition cond) {
        return PagedPostListDto.toDto(postRepository.findAllBySearch(cond));
    }


    public PostReadResponseDto read(Long id) {
        return PostReadResponseDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    // create
    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto requestDto) {
        List<PostImage> postImages = getImages(requestDto);

        Post post = postRepository.save(new Post(requestDto.getTitle(),
                requestDto.getContent(),
                categoryRepository.findByName(requestDto.getCategoryName()).orElseThrow(CategoryNotFoundException::new),
                requestDto.getItemCreateRequestDto() != null ? ItemCreateRequestDto.toEntity(requestDto.getItemCreateRequestDto()) : null,
                postImages,
                memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(AuthenticationEntryPointException::new),
                null));

        return PostCreateResponseDto.toDto(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
        deleteImagesFromServer(post);
    }

    @Transactional
    public PostUpdateResponseDto update(Long id, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        PostUpdateResponseDto responseDto = post.updatePost(requestDto);

        updateImagesToServer(requestDto, responseDto);

        return responseDto;
    }

    @Transactional
    public void like(Long id, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        likeUpOrDown(member, post);
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
    public void updateImagesToServer(PostUpdateRequestDto requestDto, PostUpdateResponseDto responseDto) {
        uploadImagesToServer(requestDto, responseDto);
        deleteImagesFromServer(responseDto);
    }

    // update - create
    private void uploadImagesToServer(PostUpdateRequestDto requestDto, PostUpdateResponseDto responseDto) {
        List<PostImageDto> addedImages = responseDto.getAddedImages();
        for (int i = 0; i < addedImages.size(); i++) {
            postFileService.upload(requestDto.getAddImages().get(i), addedImages.get(i).getUniqueName());
        }
    }

    // update - delete
    private void deleteImagesFromServer(PostUpdateResponseDto responseDto) {
        List<PostImageDto> deletePostImageDtoList = responseDto.getDeletedImages();
        deletePostImageDtoList.stream().forEach(i -> postFileService.delete(i.getUniqueName()));
    }

    private List<PostImage> getImages(PostCreateRequestDto requestDto) {
        List<PostImage> postImages;
        if (requestDto.getMultipartFiles() != null) {
            postImages = MultiPartFileToImage(requestDto.getMultipartFiles());
            uploadImagesToServer(postImages, requestDto.getMultipartFiles());
        } else {
            postImages = List.of();
        }
        return postImages;
    }

    private List<PostImage> MultiPartFileToImage(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(i -> new PostImage(i.getOriginalFilename())).collect(Collectors.toList());
    }

    private void likeUpOrDown(Member member, Post post) {
        if (post.getLikeMembers().contains(member)) {
            post.likeDown();
            post.getLikeMembers().remove(member);
        } else {
            post.likeUp();
            post.getLikeMembers().add(member);
        }
    }

}
