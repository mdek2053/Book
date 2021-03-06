package nl.tudelft.sem11b.authentication.filters;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem11b.authentication.services.UserServiceImpl;
import nl.tudelft.sem11b.data.models.UserRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomAuthenticationFilter.class)
class CustomAuthenticationFilterTest {

    @Autowired
    transient MockMvc mvc;

    @MockBean
    transient UserServiceImpl userService;

    @Autowired
    transient PasswordEncoder passwordEncoder;

    @Test
    void testSuccessfulAuthentication() throws Exception {
        when(userService.loadUserByUsername("SystemAdmin"))
                .thenReturn(new org.springframework.security.core.userdetails.User(
                        "SystemAdmin", passwordEncoder.encode("password"),
                        new ArrayList<>()));
        UserRequestModel user = new UserRequestModel("SystemAdmin", "password", "Admin");
        mvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().is2xxSuccessful());
    }
}