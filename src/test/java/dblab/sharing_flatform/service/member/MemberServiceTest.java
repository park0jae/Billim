package dblab.sharing_flatform.service.member;

import dblab.sharing_flatform.domain.address.Address;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.dto.member.MemberResponseDto;
import dblab.sharing_flatform.dto.member.MemberUpdateRequestDto;
import dblab.sharing_flatform.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dblab.sharing_flatform.factory.member.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    public void getMemberInfoTest() throws Exception {
        //given
        Member member = createMember();

        given(memberRepository.findByUsername(member.getUsername())).willReturn(Optional.of(member));

        //when
        MemberResponseDto memberInfo = memberService.getMemberInfoByUsername(member.getUsername());

        //then
        assertThat(memberInfo.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void deleteMemberTest() throws Exception {
        //given
        Member member = createMember();

        //when
        memberRepository.delete(member);

        //then
        assertThat(memberRepository.count()).isEqualTo(0);
    }

    @Test
    public void updateMemberTest() throws Exception{
        // given
        Member member = createMember();
        Address address = new Address("TestCity", "TestDistrict", "TestStreet", "TestZipcode");
        MemberUpdateRequestDto memberUpdateRequestDto = new MemberUpdateRequestDto("updatePass1!", "01011112222", address);

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

        // when
        MemberResponseDto updateMember = memberService.update(member.getId(), memberUpdateRequestDto);

        // then
        assertThat(updateMember.getPassword()).isEqualTo("updatePass1!");
    }

}
