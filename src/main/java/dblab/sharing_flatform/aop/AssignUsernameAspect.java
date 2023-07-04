package dblab.sharing_flatform.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AssignUsernameAspect {

    private final ThreadLocal<String> loggedInUserName = new ThreadLocal<>();

    @Before("@annotation(dblab.sharing_flatform.aop.AssignUsername)")
    public void assignUsername(JoinPoint join) {
    }
}

