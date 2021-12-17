package nl.tudelft.sem11b.authentication.filters;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import nl.tudelft.sem11b.authentication.controllers.AuthenticationController;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomAuthorizationFilter.class)
@PropertySource("classpath:application-dev.properties")
class CustomAuthorizationFilterTest {

    @Autowired
    transient MockMvc mvc;

    @MockBean
    transient UserServiceImpl userService;

    @MockBean
    transient AuthenticationController authenticationController;

    @Value("${spring.security.secret}")
    private transient String secret;

    @Test
    void testSuccessfulRequestWithToken() throws Exception {
        User user = new User("username", "Admin", "password");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        String token = JWT.create()
                .withSubject(user.getNetId())
                .withClaim("roles", authorities
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(secret));

        when(userService.currentUser()).thenReturn(user.toModel());
        mvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful());
    }
}