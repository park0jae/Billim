package dblab.sharing_flatform.service;

import dblab.sharing_flatform.config.security.jwt.support.TokenProvider;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.dto.member.LogInResponseDto;
import dblab.sharing_flatform.dto.member.LoginRequestDto;
import dblab.sharing_flatform.dto.member.SignUpRequestDto;
import dblab.sharing_flatform.exception.auth.LoginFailureException;
import dblab.sharing_flatform.exception.member.DuplicateSignUpMember;
import dblab.sharing_flatform.exception.member.DuplicateUsernameException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.role.RoleNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.role.RoleRepository;
import dblab.sharing_flatform.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto){

        if(memberRepository.findOneWithRolesByUsername(signUpRequestDto.getUsername()).orElseThrow(null) != null){
            throw new DuplicateSignUpMember();
        }

        Member member = new Member(signUpRequestDto.getUsername(),
                passwordEncoder.encode(signUpRequestDto.getPassword()),
                signUpRequestDto.getPhoneNumber(),
                signUpRequestDto.getAddress(),
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)),
                List.of());

        memberRepository.save(member);
    }

    public LogInResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(MemberNotFoundException::new);

        if (member != null) {
            String jwt = jwtLoginRequest(loginRequestDto);
            return LogInResponseDto.toDto(jwt);
        }
        throw new MemberNotFoundException();
    }

    private String jwtLoginRequest(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); // loadUserByUsername 메소드 실행
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 토큰 생성 및 리턴
            String jwt = tokenProvider.createAccessToken(authentication);

            if (!StringUtils.hasText(jwt)) {
                throw new LoginFailureException();
            }

            return jwt;
        } catch (BadCredentialsException e) {
            throw new LoginFailureException();
        }
    }

    // 유저 이름으로 유저 객체 권한정보 가져오기
    public Optional<Member> getMemberWithAuthorities(String username){
        return memberRepository.findOneWithRolesByUsername(username);
    }

    // SecurityContext에 저장된 username의 정보 가져오기
    public Optional<Member> getMyMemberWithAuthorities(){
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithRolesByUsername);
    }

}
