package dblab.sharing_platform.dto.post;

import dblab.sharing_platform.domain.image.PostImage;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.dto.member.MemberDto;
import dblab.sharing_platform.dto.image.PostImageDto;
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

    private List<PostImageDto> addedImages;
    private List<PostImageDto> deletedImages;


    public static PostUpdateResponseDto toDto(PostUpdateRequestDto postUpdateRequestDto, Post post, Map<String, List<PostImage>> map) {
        return new PostUpdateResponseDto(post.getId(),
                postUpdateRequestDto.getTitle(),
                postUpdateRequestDto.getContent(),
                MemberDto.toDto(post.getMember()),
                PostImageDto.toDtoList(map.getOrDefault("addList", List.of())),
                PostImageDto.toDtoList(map.getOrDefault("deleteList",List.of())));
    }

}
