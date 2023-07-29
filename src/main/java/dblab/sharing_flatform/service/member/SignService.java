package dblab.sharing_flatform.service.member;

import dblab.sharing_flatform.config.security.jwt.provider.TokenProvider;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.domain.emailAuth.EmailAuth;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.refresh.RefreshToken;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.dto.EmailAuthRequest;
import dblab.sharing_flatform.dto.member.crud.create.OAuth2MemberCreateRequestDto.OAuth2MemberCreateRequestDto;
import dblab.sharing_flatform.dto.member.crud.update.PasswordResetRequestDto;
import dblab.sharing_flatform.dto.member.login.LogInResponseDto;
import dblab.sharing_flatform.dto.member.login.LoginRequestDto;
import dblab.sharing_flatform.dto.member.crud.create.MemberCreateRequestDto;
import dblab.sharing_flatform.exception.auth.EmailAuthNotEqualsException;
import dblab.sharing_flatform.exception.auth.EmailAuthNotFoundException;
import dblab.sharing_flatform.exception.auth.LoginFailureException;
import dblab.sharing_flatform.exception.auth.DuplicateNicknameException;
import dblab.sharing_flatform.exception.auth.DuplicateUsernameException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.auth.NotEqualsPasswordToVerifiedException;
import dblab.sharing_flatform.exception.role.RoleNotFoundException;
import dblab.sharing_flatform.repository.emailAuth.EmailAuthRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.refresh.RefreshTokenRepository;
import dblab.sharing_flatform.repository.role.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;
    private final EmailAuthRepository emailAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository tokenRepository;

    @Transactional
    public void signUp(MemberCreateRequestDto requestDto){
        validateDuplicateUsernameAndNickname(requestDto);
        validateEmailAuthKey(requestDto, "SIGN-UP");

        Member member = new Member(requestDto.getUsername(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getNickname(),
                requestDto.getPhoneNumber(),
                requestDto.getAddress(),
                "None",
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)));

        memberRepository.save(member);
        emailAuthRepository.deleteByEmail(requestDto.getUsername());
    }

    @Transactional
    public void resetPassword(PasswordResetRequestDto requestDto) {
        Member member = memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(MemberNotFoundException::new);
        validateEmailAuthKey(requestDto, "RESET-PASSWORD");
        validatePasswordEqualsVerifyPassword(requestDto);

        member.updatePassword(passwordEncoder.encode(requestDto.getPassword()));

        emailAuthRepository.deleteByEmail(requestDto.getUsername());
    }

    @Transactional
    public void oAuth2Signup(OAuth2MemberCreateRequestDto requestDto) {
        Member member = new Member(requestDto.getEmail(),
                passwordEncoder.encode(requestDto.getEmail()),
                null,
                null,
                null,
                requestDto.getProvider(),
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)));
        memberRepository.save(member);
    }

    public LogInResponseDto oauth2Login(OAuth2MemberCreateRequestDto requestDto) {
        List<String> tokens = jwtLoginRequest(new LoginRequestDto(requestDto.getEmail(), requestDto.getEmail()));
        return LogInResponseDto.toDto(tokens.get(0), tokens.get(1));
    }

    public LogInResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(MemberNotFoundException::new);

        if (member != null) {
            List<String> tokens = jwtLoginRequest(requestDto);
            return LogInResponseDto.toDto(tokens.get(0), tokens.get(1));
        }
        throw new MemberNotFoundException();
    }

    private List<String> jwtLoginRequest(LoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword());
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            String accessToken = tokenProvider.createToken(authentication, "accessToken");
            String refreshToken = tokenProvider.createToken(authentication, "refreshToken");

            Optional<RefreshToken> findToken = tokenRepository.findByUsername(requestDto.getUsername());

            if(findToken.isPresent()) {
                findToken.get().changeToken(refreshToken);
                tokenRepository.save(findToken.get().changeToken(refreshToken));
            }else {
                RefreshToken newToken = RefreshToken.createToken(requestDto.getUsername(),refreshToken);
                tokenRepository.save(newToken);
            }

            List<String> tokens = new ArrayList<>();
            tokens.add(accessToken);
            tokens.add(refreshToken);

            if (!StringUtils.hasText(accessToken)) {
                throw new LoginFailureException();
            }
            return tokens;
        } catch (BadCredentialsException e) {
            throw new LoginFailureException();
        }
    }

    private void validateDuplicateUsernameAndNickname(MemberCreateRequestDto requestDto) {
        if (memberRepository.existsByUsername(requestDto.getUsername())) {
            throw new DuplicateUsernameException();
        } else if (memberRepository.existsByNickname(requestDto.getNickname())) {
            throw new DuplicateNicknameException();
        }
    }

    private void validateEmailAuthKey(EmailAuthRequest requestDto, String purpose) {
        EmailAuth emailAuth = emailAuthRepository.findByEmailAndPurpose(requestDto.getUsername(), purpose).orElseThrow(EmailAuthNotFoundException::new);
        if (!emailAuth.getKey().equals(requestDto.getAuthKey())) {
            throw new EmailAuthNotEqualsException();
        }
    }
    private void validatePasswordEqualsVerifyPassword(PasswordResetRequestDto requestDto) {
        if (!requestDto.getPassword().equals(requestDto.getVerifyPassword())) {
            throw new NotEqualsPasswordToVerifiedException();
        }
    }
}
