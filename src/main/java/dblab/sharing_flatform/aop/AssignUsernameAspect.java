package dblab.sharing_flatform.aop;

import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.dto.post.crud.create.PostCreateRequestDto;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AssignUsernameAspect {

    @Before("@annotation(dblab.sharing_flatform.aop.AssignUsername)")
    public void assignUsername(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        log.info("args = {}", args);

        Arrays.stream(args)
                .forEach(arg -> {
                    try {
                        arg.getClass().getMethod("setUsername", String.class)
                                .invoke(arg, SecurityUtil.getCurrentUsername().get());
                    } catch (NoSuchMethodException e) { // 모든 arg에 setUsername 메소드가 없는 경우
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


}

