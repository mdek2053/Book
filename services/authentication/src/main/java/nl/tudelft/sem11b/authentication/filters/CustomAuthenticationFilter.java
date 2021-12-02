package nl.tudelft.sem11b.authentication.filters;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



/**
 * Provides a custom filter to use during authentication.
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Handles the case when authentication is attempted by a user.
     * @param request contains the http request of the authentication of a user.
     * @param response contains the http response of the authentication of a user.
     * @return an object of class Authentication containing data about the authentication.
     * @throws AuthenticationException when the Authentication object is invalid.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
                                                throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        //System.out.println("??? password " + authenticationToken.getCredentials().toString());
        Authentication auth =  authenticationManager.authenticate(authenticationToken);
        //System.out.println("is auth = " + auth.isAuthenticated());
        return auth;
    }

    /**
     * Handles the case when the authentication is successful.
     * @param request contains the http request of the authentication of a user.
     * @param response contains the http response of the authentication of a user.
     * @param chain contains a chain of filters which can be applied in order.
     * @param authResult contains info which was generated from the successful authentication.
     * @throws IOException when input or output functions do not function as expected.
     * @throws ServletException when a servlet encounters difficulty functioning in the method.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)
                                            throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(Algorithm.HMAC256("super-secret"));
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), "token: " + token);
    }
}
