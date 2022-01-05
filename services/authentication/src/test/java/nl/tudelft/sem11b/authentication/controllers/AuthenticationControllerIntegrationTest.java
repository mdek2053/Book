package nl.tudelft.sem11b.authentication.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.services.UserServiceImpl;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.data.models.UserRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerIntegrationTest {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    transient MockMvc mvc;

    private static final ObjectMapper mapper = new JsonMapper();
    Long adminId;
    User admin =  new User("User", "Admin", "Password");
    UserRequestModel adminRequestModel =
            new UserRequestModel(admin.getNetId(), admin.getPassword(), "Admin");
    String token;

    @BeforeEach
    void setUp() throws Exception {
        adminId = userService.addUser(admin.getNetId(), admin.getPassword(), Roles.Admin);
        admin.setId(adminId);

        MvcResult mvcAuthResult = mvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(adminRequestModel)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Map<String, String> response = mapper.readValue(
                mvcAuthResult.getResponse().getContentAsString(),
                new TypeReference<Map<String, String>>() {});
        token = response.get("token");
    }

    @Test
    void testGetMe() throws Exception {

        MvcResult mvcGetMe = mvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        UserModel expected = admin.toModel();
        UserModel actual = mapper.readValue(
                mvcGetMe.getResponse().getContentAsString(),
                new TypeReference<UserModel>() {});
        assertEquals(expected, actual);
    }

    @Test
    void testPostUser() throws Exception {

        UserRequestModel userRequestModel =
                new UserRequestModel("new_user", "new_password", "Employee");
        MvcResult mvcResult = mvc.perform(post("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequestModel)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void testGetAllUsers() throws Exception {
        Long newUserId = userService.addUser("new_user", "new_password", Roles.Employee);
        User newUser = new User(newUserId, "new_user", "Employee", "new_password");
        List<UserModel> expected = Arrays.asList(newUser.toModel(), admin.toModel());

        MvcResult mvcResult = mvc.perform(get("/users/all")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        List<UserModel> actual = mapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<UserModel>>() {});

        assertThat(actual).hasSameElementsAs(expected);
    }

}