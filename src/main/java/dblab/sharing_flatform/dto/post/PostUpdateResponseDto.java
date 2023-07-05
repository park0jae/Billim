package dblab.sharing_flatform.dto.post;

import dblab.sharing_flatform.domain.image.Image;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.member.MemberDto;
import dblab.sharing_flatform.dto.post.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateResponseDto {

    private Long id;

    private String title;

    private String content;

    private MemberDto memberDto;

    private List<ImageDto> addedImages;
    private List<ImageDto> deletedImages;


    public static PostUpdateResponseDto toDto(PostUpdateRequestDto postUpdateRequestDto, Post post, Map<String, List<Image>> map) {
        return new PostUpdateResponseDto(post.getId(),
                postUpdateRequestDto.getTitle(),
                postUpdateRequestDto.getContent(),
                MemberDto.toDto(post.getMember()),
                ImageDto.toDtoList(map.getOrDefault("addList", List.of())),
                ImageDto.toDtoList(map.getOrDefault("deleteList",List.of())));
    }


}
