package dblab.sharing_flatform.controller.exception;

import dblab.sharing_flatform.exception.auth.ValidateTokenException;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_flatform.exception.guard.GuardException;
import dblab.sharing_flatform.exception.token.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


@ApiIgnore
@Slf4j
@RestController
public class ExceptionController {

    @GetMapping("/exception/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException();
    }

    @GetMapping("/exception/entry-point")
    public void authenticateException() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/exception/invalid-token")
    public void validateTokenException() {
        throw new ValidateTokenException();
    }

    @GetMapping("/exception/guard")
    public void guardException() {
        throw new GuardException();
    }

    @GetMapping("/exception/no-token")
    public void tokenNotFoundException() {
        throw new TokenNotFoundException();
    }
}
