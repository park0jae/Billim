package dblab.sharing_flatform.dto.member;


import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.role.RoleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long memberId;
    private String nickname;
    private RoleType memberRoles;

    public MemberDto(Long id, String nickname, Member member) {
        this.memberId = id;
        this.nickname = nickname;
        this.memberRoles = member.getRoles().get(0).getRole().getRoleType();
    }

    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getNickname(), member.getRoles().get(0).getRole().getRoleType());
    }
}
