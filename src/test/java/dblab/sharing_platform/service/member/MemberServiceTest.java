package dblab.sharing_platform.service.member;

import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.dto.member.MemberDto;
import dblab.sharing_platform.dto.member.MemberPrivateDto;
import dblab.sharing_platform.dto.member.MemberProfileDto;
import dblab.sharing_platform.dto.member.MemberPagingCondition;
import dblab.sharing_platform.dto.member.MemberUpdateRequestDto;
import dblab.sharing_platform.exception.member.MemberNotFoundException;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dblab.sharing_platform.factory.category.CategoryFactory.*;
import static dblab.sharing_platform.factory.item.ItemFactory.createItem;
import static dblab.sharing_platform.factory.member.MemberFactory.*;
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
    PostRepository postRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    Member member;
    @BeforeEach
    public void beforeEach(){
        member = createMember();
    }

    @Test
    @DisplayName("본인 정보 가져오기 테스트")
    public void getMemberInfoTest() throws Exception {
        //given
        given(memberRepository.findByUsername(member.getUsername())).willReturn(Optional.of(member));

        //when
        MemberPrivateDto memberInfo = memberService.readCurrentUserInfoByUsername(member.getUsername());

        //then
        assertThat(memberInfo.getUsername()).isEqualTo("username");
    }

    @Test
    @DisplayName("회원 정보 가져오기 테스트")
    public void readMemberProfileTest(){
        // Given
        List<Post> posts = new ArrayList<>();
        member = createMemberWithRole();
        posts.add(new Post("title", "content", createCategory(), createItem(), List.of(), member));

        given(postRepository.findAllWithMemberByNickname(member.getNickname())).willReturn(posts);
        given(memberRepository.findByNickname(member.getNickname())).willReturn(Optional.of(member));

        // Then
        MemberProfileDto result = memberService.readMemberProfileByNickname(member.getNickname());

        // When
        assertThat(result.getNickname()).isEqualTo("nickname");
        assertThat(result.getPosts().get(0).getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    @DisplayName("회원 이름 조건 검색 테스트")
    public void findAllBySearchTest(){
        // Given
        List<Member> members = new ArrayList<>();
        members.add(createMemberWithRole());
        members.add(createRenderMember());

        MemberPagingCondition cond = new MemberPagingCondition(0, 10, "nickname");

        given(memberRepository.findAllBySearch(cond)).willReturn(new PageImpl<>(List.of(MemberDto.toDto(members.get(0))), PageRequest.of(cond.getPage(), cond.getSize()),1));

        // When
        Page<MemberDto> result = memberRepository.findAllBySearch(cond);

        // Then
        assertThat(result.getContent()).hasSize(1);
        MemberDto memberDto = result.getContent().get(0);
        assertThat(memberDto.getNickname()).isEqualTo("nickname");
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    public void deleteMemberTest() throws Exception {
        // Given
        given(memberRepository.findByUsername(member.getUsername())).willReturn(Optional.of(member));

        // When
        memberService.deleteMemberByUsername(member.getUsername());

        // Then
        verify(memberRepository).delete(member);
    }

    @Test
    @DisplayName("회원 수정 테스트")
    public void updateMemberTest() throws Exception{
        // Given
        MemberUpdateRequestDto memberUpdateRequestDto = new MemberUpdateRequestDto("updatePass1!", "updateNickName", "updatedPN", member.getAddress(), "hi~", null);
        given(memberRepository.findByUsername(member.getUsername())).willReturn(Optional.of(member));

        // When
        MemberPrivateDto updateMember = memberService.updateMember(member.getUsername(), memberUpdateRequestDto);

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
        assertThatThrownBy(() -> memberService.readCurrentUserInfoByUsername(username)).isInstanceOf(MemberNotFoundException.class);
    }



}
