package dblab.sharing_flatform.aop;

import dblab.sharing_flatform.config.security.details.MemberDetails;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.util.SecurityUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AssignUsernameAspect {
    private ThreadLocal<String> usernameThreadLocal = new ThreadLocal<>();

    @Before("@annotation(dblab.sharing_flatform.aop.AssignUsername)")
    public void assignUsername(JoinPoint joinPoint) {
        String username = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        usernameThreadLocal.set(username);
    }

    @AfterReturning("execution(*dblab.sharing_flatform.controller.message")
    public void clearUsername() {
        usernameThreadLocal.remove();
    }
}
