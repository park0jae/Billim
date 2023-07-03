package dblab.sharing_flatform.config.security.jwt.filter;

import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.ValidateTokenException;
<<<<<<< HEAD
import io.jsonwebtoken.JwtException;
=======
import lombok.extern.slf4j.Slf4j;
>>>>>>> 4da1ad761b07e7b698e99803ac35819eb9dfa744
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
        }
    }

}
