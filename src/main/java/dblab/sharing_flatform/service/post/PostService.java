package dblab.sharing_flatform.service.post;

import dblab.sharing_flatform.domain.image.Image;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.item.crud.create.ItemCreateRequestDto;
import dblab.sharing_flatform.dto.post.crud.create.PostCreateRequestDto;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import dblab.sharing_flatform.dto.post.crud.read.response.PagedPostListDto;
import dblab.sharing_flatform.dto.post.crud.read.response.PostReadResponseDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateRequestDto;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateResponseDto;
import dblab.sharing_flatform.dto.image.ImageDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeRequestDto;
import dblab.sharing_flatform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_flatform.exception.category.CategoryNotFoundException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.item.ItemRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import dblab.sharing_flatform.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    public PagedPostListDto readAll(PostPagingCondition cond) {
        return PagedPostListDto.toDto(postRepository.findAllBySearch(cond));
    }

    public PostReadResponseDto read(Long id) {
        return PostReadResponseDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    // create
    @Transactional
    public void create(PostCreateRequestDto postCreateRequestDto) {
        List<Image> images = getImages(postCreateRequestDto);

        postRepository.save(new Post(postCreateRequestDto.getTitle(),
                postCreateRequestDto.getContent(),
                categoryRepository.findByName(postCreateRequestDto.getCategoryName()).orElseThrow(CategoryNotFoundException::new),
                postCreateRequestDto.getItemCreateRequestDto() != null ? itemRepository.save(ItemCreateRequestDto.toEntity(postCreateRequestDto.getItemCreateRequestDto())) : null,
                images,
                memberRepository.findByUsername(postCreateRequestDto.getUsername()).orElseThrow(AuthenticationEntryPointException::new),
                null
                )
        );
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

    private List<Image> getImages(PostCreateRequestDto postCreateRequestDto) {
        List<Image> images;
        if (postCreateRequestDto.getItemCreateRequestDto() != null) {
            images = MultiPartFileToImage(postCreateRequestDto.getMultipartFiles());
            uploadImagesToServer(images, postCreateRequestDto.getMultipartFiles());
        } else {
            images = List.of();
        }
        return images;
    }

    private List<Image> MultiPartFileToImage(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(i -> new Image(i.getOriginalFilename())).collect(Collectors.toList());
    }
}
