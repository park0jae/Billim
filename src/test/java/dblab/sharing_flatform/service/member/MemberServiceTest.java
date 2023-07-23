package dblab.sharing_flatform.service.member;

import dblab.sharing_flatform.domain.embedded.address.Address;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.dto.member.MemberPrivateDto;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static dblab.sharing_flatform.factory.member.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    Member member;
    @BeforeEach
    public void beforeEach(){
        member = createMember();
    }

    @Test
    @DisplayName("회원 정보 가져오기 테스트")
    public void getMemberInfoTest() throws Exception {
        //given
        given(memberRepository.findByUsername(member.getUsername())).willReturn(Optional.of(member));

        //when
        MemberPrivateDto memberInfo = memberService.readMyInfo(member.getUsername());

        //then
        assertThat(memberInfo.getUsername()).isEqualTo("username");
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    public void deleteMemberTest() throws Exception {
        // Given
        given(memberRepository.findByUsername(member.getUsername())).willReturn(Optional.of(member));

        // When
        memberService.delete(member.getUsername());

        // Then
        verify(memberRepository).delete(member);
    }

    @Test
    @DisplayName("회원 수정 테스트")
    public void updateMemberTest() throws Exception{
        // Given
        MemberUpdateRequestDto memberUpdateRequestDto = new MemberUpdateRequestDto(member.getUsername(),"updatePass1!", "updatedPN", member.getAddress(), "hi~", null);
        given(memberRepository.findByUsername(member.getUsername())).willReturn(Optional.of(member));

        // When
        MemberPrivateDto updateMember = memberService.update(member.getUsername(), memberUpdateRequestDto);

        // Then
        assertThat(updateMember.getPhoneNumber()).isEqualTo("updatedPN");
    }

    @Test
    @DisplayName("회원 존재 X, 예외 테스트")
    public void memberNotFoundException(){
        // Given
        String username = "fakeUser";
        given(memberRepository.findByUsername(username)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.readMyInfo(username)).isInstanceOf(MemberNotFoundException.class);
    }

}
