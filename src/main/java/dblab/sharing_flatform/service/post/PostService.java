package dblab.sharing_flatform.service.post;

import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.image.Image;
import dblab.sharing_flatform.domain.item.Item;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.item.ItemCreateRequestDto;
import dblab.sharing_flatform.dto.post.PostCreateRequestDto;
import dblab.sharing_flatform.exception.category.CategoryNotFoundException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.item.ItemRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        List<Image> images = postCreateRequestDto.getImages().
                stream().map(i -> new Image(i.getOriginalFilename())).collect(Collectors.toList());

        // 글 저장
        postRepository.save(new Post(postCreateRequestDto.getTitle(),
                postCreateRequestDto.getContent(),
                categoryRepository.findByName(postCreateRequestDto.getCategoryName()).orElseThrow(CategoryNotFoundException::new),
                itemRepository.save(ItemCreateRequestDto.toEntity(postCreateRequestDto.getItemCreateRequestDto())),
                images,
                memberRepository.findByUsername(postCreateRequestDto.getUsername()).orElseThrow(MemberNotFoundException::new)));

        // 로컬 서버에 글에 올린 이미지 업로드
        uploadImages(images, postCreateRequestDto.getImages());

    }
    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        for (int i = 0; i < images.size(); i++) {
            fileService.upload(fileImages.get(i), images.get(i).getUniqueName());
        }
    }
}
