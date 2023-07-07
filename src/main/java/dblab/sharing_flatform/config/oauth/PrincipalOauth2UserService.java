package dblab.sharing_flatform.config.oauth;

import dblab.sharing_flatform.config.oauth.provider.KakaoUserInfo;
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
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        switch (registrationId){
            case "kakao":
                oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
                break;
        }

        Optional<Member> memberEntity = memberRepository.findByProvider(oAuth2UserInfo.getProvider());

        Member member;
        if(memberEntity.isPresent()){
            member = memberEntity.get();
            memberRepository.save(member);
        }else {
            member = new Member(oAuth2UserInfo.getEmail(),
                    null,
                    oAuth2UserInfo.getPhoneNumber(),
                    oAuth2UserInfo.getAddress(),
                    "None",
                    List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)),
                    List.of());
            memberRepository.save(member);
        }

        return new MemberDetails(Long.toString(member.getId()), member.getUsername(), null, List.of(), oAuth2User.getAttributes());
    }


}
