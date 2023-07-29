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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ValidateTokenException e) {
            response.sendRedirect("/exception/invalid-token");
            return;
        } catch (GuardException e) {
            response.sendRedirect("/exception/guard");
            return;
        } catch (TokenNotFoundException e){
            response.sendRedirect("/exception/no-token");
        }
    }

}
