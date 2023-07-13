package dblab.sharing_flatform.dto.member;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.dto.item.ItemDto;
import dblab.sharing_flatform.dto.post.PostDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfileDto {

    private String username;
    private String introduce;
    private List<PostDto> posts;

    public static MemberProfileDto toDto(Member member) {
        return new MemberProfileDto(member.getUsername(), member.getIntroduce(), PostDto.toDtoList(member.getPosts()));
    }
}