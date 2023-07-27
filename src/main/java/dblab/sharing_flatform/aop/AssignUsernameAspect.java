package dblab.sharing_flatform.aop;

import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.auth.IllegalAuthenticationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class AssignUsernameAspect {


}

