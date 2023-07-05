package dblab.sharing_flatform.dto.member;


import dblab.sharing_flatform.domain.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long id;
    private String username;

    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getUsername());
    }

}
