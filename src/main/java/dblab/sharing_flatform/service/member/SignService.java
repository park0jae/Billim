package dblab.sharing_flatform.service.member;

import dblab.sharing_flatform.config.security.jwt.provider.TokenProvider;
import dblab.sharing_flatform.domain.emailAuth.EmailAuth;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.refresh.RefreshToken;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.dto.member.*;
import dblab.sharing_flatform.exception.auth.*;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.role.RoleNotFoundException;
import dblab.sharing_flatform.repository.emailAuth.EmailAuthRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.refresh.RefreshTokenRepository;
import dblab.sharing_flatform.repository.role.RoleRepository;
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

import java.util.ArrayList;
import java.util.List;

import static dblab.sharing_flatform.domain.refresh.RefreshToken.createToken;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {

    public static final String AUTH_KEY_SIGN_UP = "SIGN-UP";
    public static final String AUTH_KEY_RESET_PASSWORD = "RESET-PASSWORD";
    public static final String NONE = "None";

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
        validateEmailAuthKey(requestDto, AUTH_KEY_SIGN_UP);

        Member member = new Member(requestDto.getUsername(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getNickname(),
                requestDto.getPhoneNumber(),
                requestDto.getAddress(),
                NONE,
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)));

        memberRepository.save(member);
        emailAuthRepository.deleteByEmail(requestDto.getUsername());
    }

    @Transactional
    public void resetPassword(PasswordResetRequestDto requestDto) {
        Member member = memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(MemberNotFoundException::new);
        validateEmailAuthKey(requestDto, AUTH_KEY_RESET_PASSWORD);
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

    @Transactional
    public LogInResponseDto oauth2Login(OAuth2MemberCreateRequestDto requestDto) {
        List<String> tokens = jwtLoginRequest(new LoginRequestDto(requestDto.getEmail(), requestDto.getEmail()));
        return LogInResponseDto.toDto(tokens.get(0), tokens.get(1));
    }

    @Transactional
    public LogInResponseDto login(LoginRequestDto requestDto) {
        memberRepository.findByUsername(requestDto.getUsername()).orElseThrow(MemberNotFoundException::new);
        List<String> tokens = jwtLoginRequest(requestDto);
        return LogInResponseDto.toDto(tokens.get(0), tokens.get(1));
    }

    @Transactional
    public List<String> jwtLoginRequest(LoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword());
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            String accessToken = tokenProvider.createToken(authentication, TokenProvider.ACCESS);
            String refreshToken = tokenProvider.createToken(authentication, TokenProvider.REFRESH);

            RefreshToken findToken = tokenRepository.findByUsername(requestDto.getUsername()).orElse(null);

            if (findToken == null) {
                tokenRepository.save(createToken(requestDto.getUsername(), refreshToken));
            } else {
                findToken.changeToken(refreshToken);
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
