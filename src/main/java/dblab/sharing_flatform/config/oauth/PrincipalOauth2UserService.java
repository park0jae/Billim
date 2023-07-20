package dblab.sharing_flatform.config.oauth;

import dblab.sharing_flatform.config.oauth.provider.GoogleUserInfo;
import dblab.sharing_flatform.config.oauth.provider.KakaoUserInfo;
import dblab.sharing_flatform.config.oauth.provider.NaverUserInfo;
import dblab.sharing_flatform.config.oauth.provider.OAuth2UserInfo;
import dblab.sharing_flatform.config.security.details.MemberDetails;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.exception.role.RoleNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        switch (registrationId){
            case "kakao":
                oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
                break;
            case "naver":
                oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
                break;
            case "google":
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }

        Optional<Member> memberEntity = memberRepository.findByProviderAndUsername(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getEmail());

        log.info("memberEntity={}", memberEntity);

        Member member = saveOrUpdate(oAuth2UserInfo, memberEntity);

        log.info("oauth2UserInfo={}" , oAuth2UserInfo);

        return new MemberDetails(Long.toString(member.getId()), member.getUsername(), null, List.of(), oAuth2User.getAttributes());
    }

    public Member saveOrUpdate(OAuth2UserInfo oAuth2UserInfo, Optional<Member> memberEntity) {
        Member member;
        if (memberEntity.isPresent()) {
            member = memberEntity.get();
        } else {
            member = new Member(oAuth2UserInfo.getEmail(),
                    "",
                    oAuth2UserInfo.getPhoneNumber(),
                    oAuth2UserInfo.getAddress(),
                    oAuth2UserInfo.getProvider(),
                    List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)));
            memberRepository.save(member);
        }
        return member;
    }
}