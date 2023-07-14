package dblab.sharing_flatform.domain.member;

import dblab.sharing_flatform.domain.address.Address;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static dblab.sharing_flatform.factory.member.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberTest {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    public void createMemberTest() throws Exception {
        //given
        Member member = createMember();

        //then
        assertThat(member).isNotNull();
    }

    @Test
    public void updateMemberTest() throws Exception {
        //given
        Member member = createMember();

        String phoneNumber = "TestNumber";
        String password = "TestPassword";
        String introduce = "hi~ I'm test member!";
        Address address = new Address("TestCity", "TestDistrict", "TestStreet", "TestZipcode");

        //when
<<<<<<< HEAD
        member.updateMember(new MemberUpdateRequestDto(password, phoneNumber, address, introduce), password);
=======
        member.updateMember(new MemberUpdateRequestDto(member.getPassword(), phoneNumber, address, introduce, null), member.getPassword());
>>>>>>> ccaed9ae2dec6f1b8d24e99ad16655e2332596c2

        //then
        assertThat(member.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(member.getAddress()).isEqualTo(address);
        assertThat(member.getIntroduce()).isEqualTo(introduce);
    }
    
    @Test  
    public void initPostsTest() throws Exception {
        // given
        Member member = createMember();

        // when
        List<Post> posts = member.getPosts();

        // then
        posts.stream().forEach(p ->
                Assertions.assertThat(p.getMember()).isEqualTo(member));

    }
}