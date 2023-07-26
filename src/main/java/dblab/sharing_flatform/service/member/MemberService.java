package dblab.sharing_flatform.service.member;


import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.member.MemberPrivateDto;
import dblab.sharing_flatform.dto.member.MemberProfileDto;
import dblab.sharing_flatform.dto.member.crud.read.request.MemberPagingCondition;
import dblab.sharing_flatform.dto.member.crud.read.response.PagedMemberListDto;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import dblab.sharing_flatform.dto.member.crud.update.OAuthMemberUpdateRequestDto;
import dblab.sharing_flatform.dto.post.PostDto;
import dblab.sharing_flatform.exception.member.DuplicateNicknameException;
import dblab.sharing_flatform.exception.member.DuplicateUsernameException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import dblab.sharing_flatform.service.file.MemberFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final MemberFileService postFileService;

    public MemberPrivateDto readMyInfo(String username){
        return MemberPrivateDto.toDto(memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new));
    }

    public MemberProfileDto readMemberProfile(String nickname) {
        List<Post> posts = postRepository.findAllWithMemberByNickname(nickname);
        return MemberProfileDto.toDto(memberRepository.findByNickname(nickname).orElseThrow(MemberNotFoundException::new), PostDto.toDtoList(posts));
    }

    public PagedMemberListDto readAll(MemberPagingCondition cond) {
        return PagedMemberListDto.toDto(memberRepository.findAllBySearch(cond));
    }

    @Transactional
    public void delete(String username){
        memberRepository.delete(memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new));
    }

    @Transactional
    public MemberPrivateDto update(String username, MemberUpdateRequestDto requestDto){
        Member member = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);
        memberUpdate(requestDto, member);

        return MemberPrivateDto.toDto(member);
    }

    @Transactional
    public MemberPrivateDto oauthMemberUpdate(String username, OAuthMemberUpdateRequestDto requestDto){
        Member member = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);
        oAuthMemberUpdate(requestDto, member);

        return MemberPrivateDto.toDto(member);
    }

    private String encodeRawPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void memberUpdate(MemberUpdateRequestDto requestDto, Member member) {
        validateDuplicateUsernameAndNickname(requestDto.getUsername(), requestDto.getNickname());
        String profileImage = member.updateMember(requestDto, encodeRawPassword(requestDto.getPassword()));
        profileImageServerUpdate(requestDto.getImage(), member, profileImage);
    }

    private void oAuthMemberUpdate(OAuthMemberUpdateRequestDto requestDto, Member member) {
        validateDuplicateUsernameAndNickname(null, requestDto.getNickname());
        String profileImage = member.updateOAuthMember(requestDto);
        profileImageServerUpdate(requestDto.getImage(), member, profileImage);
    }

    private void profileImageServerUpdate(MultipartFile file, Member member, String profileImage) {
        if (file != null) {
            postFileService.upload(file, member.getProfileImage().getUniqueName());
        }
        if (profileImage != null) {
            postFileService.delete(profileImage);
        }
    }

    public void validateDuplicateUsernameAndNickname(String username, String nickname) {
        if (username != null && memberRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException();
        } else if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }
    }

}
