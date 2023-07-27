package dblab.sharing_flatform.config.security.util;

import dblab.sharing_flatform.config.security.details.MemberDetails;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.role.Role;
import dblab.sharing_flatform.domain.role.RoleType;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_flatform.exception.auth.IllegalAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
public class SecurityUtil {

    public static Optional<String> getCurrentUsername(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            log.info("Security Context에 인증정보가 없습니다.");
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof MemberDetails){
            MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
            return Optional.ofNullable(memberDetails.getUsername());
        } else if (authentication.getPrincipal() instanceof String) {
            throw new AuthenticationEntryPointException();
        }

        throw new IllegalAuthenticationException();
    }

    public static Optional<String> getCurrentUserId(){
        Authentication authentication = getAuthenticationFromContext();
        if(authentication == null){
            log.info("No Authentication Found");
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof MemberDetails) {
            MemberDetails memberDetails = (MemberDetails) principal;
            return Optional.ofNullable(memberDetails.getId());
        } else if (principal instanceof String) {
            throw new AuthenticationEntryPointException();
        }

        throw new IllegalAuthenticationException();
    }

    // 컨텍스트에 담긴 Authentication 객체가 인증된 상태인지 검사
    public static boolean isAuthenticated(){
        Authentication authentication = getAuthenticationFromContext();
        return authentication.isAuthenticated() && authentication instanceof UsernamePasswordAuthenticationToken;
    }

    // 컨텍스트에 담긴 Authentication 으로부터 Roles 추출
    public static List<RoleType> extractMemberRolesFromContext() {
        Authentication authentication = getAuthenticationFromContext();
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();

        return authorities.stream().map(authority -> authority.getAuthority()).map(auth -> RoleType.valueOf(auth)).collect(Collectors.toList());
    }

    private static Authentication getAuthenticationFromContext() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getCurrentUsernameCheck(){
        return getCurrentUsername().orElseThrow(AccessDeniedException::new);
    }
    public static String getCurrentUserIdCheck(){
        return getCurrentUserId().orElseThrow(AccessDeniedException::new);
    }
}
