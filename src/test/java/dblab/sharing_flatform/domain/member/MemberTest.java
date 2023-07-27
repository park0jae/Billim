package dblab.sharing_flatform.domain.member;

import dblab.sharing_flatform.domain.embedded.address.Address;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static dblab.sharing_flatform.factory.member.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("회원 저장")
    public void createMemberTest() throws Exception {
        //given
        Member member = createMember();
        //then
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("회원 정보 수정")
    public void updateMemberTest() throws Exception {
        //given
        Member member = createMember();

        String phoneNumber = "TestNumber";
        String password = "TestPassword";
        String nickname = "nickname";
        String introduce = "hi~ I'm test member!";
        Address address = new Address("TestCity", "TestDistrict", "TestStreet", "TestZipcode");

        //when
        member.updateMember(new MemberUpdateRequestDto(password, nickname, phoneNumber, address, introduce, null), password);

        //then
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(member.getAddress()).isEqualTo(address);
        assertThat(member.getIntroduce()).isEqualTo(introduce);
    }
}