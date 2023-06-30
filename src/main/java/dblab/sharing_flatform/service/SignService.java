package dblab.sharing_flatform.service;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.dto.member.SignUpRequestDto;
import dblab.sharing_flatform.exception.member.DuplicateSignUpMember;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto){
        if(memberRepository.findOneWithRolesByUsername(signUpRequestDto.getUsername()).orElse(null)!= null){
            throw new DuplicateSignUpMember();
        }

        Member member = new Member(signUpRequestDto.getUsername(),
                passwordEncoder.encode(signUpRequestDto.getPassword()),
                signUpRequestDto.getPhoneNumber(),
                signUpRequestDto.getAddress(),
                List.of(new Role(RoleType.USER)),
                null
                );

        memberRepository.save(member);
    }

    // 유저 이름으로 유저 객체 권한정보 가져오기
    public Optional<Member> getMemberWithAuthorities(String username){
        return memberRepository.findOneWithRolesByUsername(username);
    }

    // SecurityContext에 저장된 username의 정d보 가져오기
    public Optional<Member> getMyMemberWithAuthorities(){
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithRolesByUsername);
    }

}
