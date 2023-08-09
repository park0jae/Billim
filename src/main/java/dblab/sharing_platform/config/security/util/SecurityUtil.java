package dblab.sharing_platform.config.security.util;

import dblab.sharing_platform.config.security.details.MemberDetails;
import dblab.sharing_platform.domain.role.RoleType;
import dblab.sharing_platform.exception.auth.AccessDeniedException;
import dblab.sharing_platform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_platform.exception.auth.IllegalAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SecurityUtil {

    public static Optional<String> getCurrentUsername(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof MemberDetails){
            System.out.println("principal = " + principal);
            MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
            return Optional.ofNullable(memberDetails.getUsername());
        } else if (authentication.getPrincipal() instanceof String) {
            System.out.println("principal = " + principal);
            throw new AuthenticationEntryPointException();
        }

        throw new IllegalAuthenticationException();
    }

    public static Optional<String> getCurrentUserId(){
        Authentication authentication = getAuthenticationFromContext();
        if(authentication == null){
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
