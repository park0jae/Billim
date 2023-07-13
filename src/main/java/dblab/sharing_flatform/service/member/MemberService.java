package dblab.sharing_flatform.service.member;


import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.dto.member.MemberPrivateDto;
import dblab.sharing_flatform.dto.member.MemberProfileDto;
import dblab.sharing_flatform.dto.member.crud.update.MemberUpdateRequestDto;
import dblab.sharing_flatform.dto.member.crud.update.OAuthMemberUpdateRequestDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

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
    public MemberPrivateDto update(Long id, MemberUpdateRequestDto memberUpdateRequestDto){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        member.updateMember(memberUpdateRequestDto, encodeRawPassword(memberUpdateRequestDto.getPassword()));

        return MemberPrivateDto.toDto(member);
    }

    @Transactional
    public MemberPrivateDto oauthMemberUpdate(Long id, OAuthMemberUpdateRequestDto requestDto){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        member.updateOAuth2Member(requestDto);

        return MemberPrivateDto.toDto(member);
    }

    private String encodeRawPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
