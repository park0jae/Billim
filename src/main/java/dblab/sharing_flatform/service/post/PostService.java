package dblab.sharing_flatform.service.post;

import dblab.sharing_flatform.domain.image.PostImage;
import dblab.sharing_flatform.domain.likepost.LikePost;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.notification.NotificationType;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.image.PostImageDto;
import dblab.sharing_flatform.dto.item.ItemCreateRequestDto;
import dblab.sharing_flatform.dto.post.*;
import dblab.sharing_flatform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_flatform.exception.category.CategoryNotFoundException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.helper.NotificationHelper;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.likepost.LikePostRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.service.file.PostFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PostFileService postFileService;
    private final LikePostRepository likePostRepository;
    private final NotificationHelper notificationHelper;

    private static final String LIKE_POST_MESSAGE = "님이 이 글을 좋아합니다.";

    public PagedPostListDto readAll(PostPagingCondition cond) {
        return PagedPostListDto.toDto(postRepository.findAllByCategoryAndTitle(cond));
    }

    public PagedPostListDto readAllLikePost(PostPagingCondition cond) {
        return PagedPostListDto.toDto(likePostRepository.findAllLikesByCurrentUsername(cond));
    }

    public PagedPostListDto readAllWriteByCurrentUser(PostPagingCondition cond) {
        return PagedPostListDto.toDto(postRepository.findAllWithMemberByCurrentUsername(cond));
    }

    public PostReadResponseDto read(Long id) {
        return PostReadResponseDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    // create
    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto requestDto, String username) {
        List<PostImage> postImages = getImages(requestDto);

        Post post = new Post(requestDto.getTitle(),
                requestDto.getContent(),
                categoryRepository.findByName(requestDto.getCategoryName()).orElseThrow(CategoryNotFoundException::new),
                requestDto.getItemCreateRequestDto() != null ? ItemCreateRequestDto.toEntity(requestDto.getItemCreateRequestDto()) : null,
                postImages,
                memberRepository.findByUsername(username).orElseThrow(AuthenticationEntryPointException::new));

        postRepository.save(post);

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

        List<LikePost> likePosts = likePostRepository.findAllByPostId(post.getId());

        Optional<LikePost> likePost = validateAlreadyLikeUp(member, likePosts);

        if (likePost.isPresent()) {
            post.likeDown();
            likePosts.remove(likePost.get());
            likePostRepository.deleteByMemberIdAndPostId(member.getId(), post.getId());
        } else {
            post.likeUp();
            likePosts.add(new LikePost(member, post));
            likePostRepository.save(new LikePost(member, post));
            Member writeMember = memberRepository.findByUsername(post.getMember().getUsername()).orElseThrow(MemberNotFoundException::new);
            notificationHelper.notificationIfSubscribe(member, writeMember, NotificationType.LIKE, LIKE_POST_MESSAGE);
        }
    }

    private Optional<LikePost> validateAlreadyLikeUp(Member member, List<LikePost> likePosts) {
        return likePosts.stream()
                .filter(lp -> lp.getMember().equals(member))
                .findFirst();
    }


}
