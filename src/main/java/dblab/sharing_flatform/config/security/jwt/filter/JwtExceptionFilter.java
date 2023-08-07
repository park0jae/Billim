package dblab.sharing_flatform.config.security.jwt.filter;

import dblab.sharing_flatform.exception.auth.ValidateTokenException;
import dblab.sharing_flatform.exception.guard.GuardException;
import dblab.sharing_flatform.exception.token.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    private static final String EXCEPTION_INVALID_TOKEN = "/exception/invalid-token";
    private static final String EXCEPTION_GUARD = "/exception/guard";
    private static final String EXCEPTION_NO_TOKEN = "/exception/no-token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ValidateTokenException e) {
            response.sendRedirect(EXCEPTION_INVALID_TOKEN);
            return;
        } catch (GuardException e) {
            response.sendRedirect(EXCEPTION_GUARD);
            return;
        } catch (TokenNotFoundException e){
            response.sendRedirect(EXCEPTION_NO_TOKEN);
            return;
        }
    }

}
