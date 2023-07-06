package dblab.sharing_flatform.dto.member;

import dblab.sharing_flatform.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberPrivateDto {

    private Long id;

    private String username;

    private String password;

    private String phoneNumber;

    public static MemberPrivateDto toDto(Member member) {
        return new MemberPrivateDto(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getPhoneNumber());
    }
}