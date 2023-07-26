package dblab.sharing_flatform.service.member;

import dblab.sharing_flatform.domain.embedded.address.Address;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.member.MemberDto;
import dblab.sharing_flatform.dto.member.MemberPrivateDto;
import dblab.sharing_flatform.dto.member.MemberProfileDto;
import dblab.sharing_flatform.dto.member.crud.read.request.MemberPagingCondition;
import dblab.sharing_flatform.dto.member.crud.read.response.PagedMemberListDto;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import dblab.sharing_flatform.dto.post.PostDto;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import dblab.sharing_flatform.dto.post.crud.read.response.PagedPostListDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.factory.category.CategoryFactory;
import dblab.sharing_flatform.factory.item.ItemFactory;
import dblab.sharing_flatform.factory.post.PostFactory;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.repository.post.QPostRepository;
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

import static dblab.sharing_flatform.factory.category.CategoryFactory.*;
import static dblab.sharing_flatform.factory.item.ItemFactory.createItem;
import static dblab.sharing_flatform.factory.member.MemberFactory.*;
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
        MemberPrivateDto memberInfo = memberService.readMyInfo(member.getUsername());

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
        MemberProfileDto result = memberService.readMemberProfile(member.getNickname());

        // When
        assertThat(result.getNickname()).isEqualTo("username");
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
        memberService.delete(member.getUsername());

        // Then
        verify(memberRepository).delete(member);
    }

    @Test
    @DisplayName("회원 수정 테스트")
    public void updateMemberTest() throws Exception{
        // Given
        MemberUpdateRequestDto memberUpdateRequestDto = new MemberUpdateRequestDto(member.getUsername(),"updatePass1!", "updateNickName", "updatedPN", member.getAddress(), "hi~", null);
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
