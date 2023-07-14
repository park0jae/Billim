package dblab.sharing_flatform.service.member;


import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.dto.member.MemberPrivateDto;
import dblab.sharing_flatform.dto.member.MemberProfileDto;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import dblab.sharing_flatform.dto.member.crud.update.OAuthMemberUpdateRequestDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.service.file.MemberFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberFileService postFileService;

    public MemberPrivateDto findMyInfo(String username){
        return MemberPrivateDto.toDto(memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new));
    }

    public MemberPrivateDto findMemberByIdOnlyAdmin(Long id){
        return MemberPrivateDto.toDto(memberRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }

    public MemberProfileDto findMemberProfile(String username) {
        return MemberProfileDto.toDto(memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new));
    }

    @Transactional
    public void delete(Long id){
        memberRepository.delete(memberRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }

    @Transactional
    public MemberPrivateDto update(Long id, MemberUpdateRequestDto requestDto){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberUpdate(requestDto, member);

        return MemberPrivateDto.toDto(member);
    }

    @Transactional
    public MemberPrivateDto oauthMemberUpdate(Long id, OAuthMemberUpdateRequestDto requestDto){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        oAuthMemberUpdate(requestDto, member);

        return MemberPrivateDto.toDto(member);
    }


    private String encodeRawPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void memberUpdate(MemberUpdateRequestDto memberUpdateRequestDto, Member member) {
        String profileImage = member.updateMember(memberUpdateRequestDto, encodeRawPassword(memberUpdateRequestDto.getPassword()));

        if (memberUpdateRequestDto.getImage() != null) {
            postFileService.upload(memberUpdateRequestDto.getImage(), member.getProfileImage().getUniqueName());
        }
        if (profileImage != null) {
            postFileService.delete(profileImage);
        }
    }

    private void oAuthMemberUpdate(OAuthMemberUpdateRequestDto requestDto, Member member) {
        String profileImage = member.updateOAuthMember(requestDto);

        if (requestDto.getImage() != null) {
            postFileService.upload(requestDto.getImage(), member.getProfileImage().getUniqueName());
        }
        if (profileImage != null) {
            postFileService.delete(profileImage);
        }
    }
}
