package nl.tudelft.sem11b.authentication.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
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
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.data.models.UserRequestModel;
import nl.tudelft.sem11b.services.GroupService;
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
class GroupControllerIntegrationTest {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    GroupService groupService;

    @Autowired
    transient MockMvc mvc;

    private static final ObjectMapper mapper = new JsonMapper();
    Long adminId;
    User admin =  new User("Admin", "Admin", "Password");
    transient User secretary = new User(1L, "Secretary", "Secretary", "abc1");
    transient User user = new User(2L, "User", "Employee", "abc2");
    UserRequestModel adminRequestModel =
            new UserRequestModel(admin.getNetId(), admin.getPassword(), "Admin");
    String token;

    GroupModel groupModel;

    @BeforeEach
    void setUp() throws Exception {
        adminId = userService.addUser(admin.getNetId(), admin.getPassword(), Roles.Admin);
        admin.setId(adminId);
        secretary.setId(userService
                .addUser(secretary.getNetId(), secretary.getPassword(), Roles.Secretary));
        user.setId(userService
                .addUser(user.getNetId(), user.getPassword(), Roles.Employee));

        groupModel = new GroupModel("new_group",
                secretary.getId(), Arrays.asList(adminId, user.getId()));

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
    void testPostGroup() throws Exception {

        MvcResult mvcResult = mvc.perform(post("/groups")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(groupModel)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        GroupModel result = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<GroupModel>() {});
        assertEquals(groupModel.getName(), result.getName());
    }

    @Test
    void testGetGroupsOfUser() throws Exception {
        groupModel.setGroupId(groupService
                .addGroup(groupModel.getName(), groupModel.getSecretary(),
                groupModel.getGroupMembers()).getGroupId());

        MvcResult mvcResult = mvc.perform(get("/groups/user/" + user.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<GroupModel> expected = Arrays.asList(groupModel);
        List<GroupModel> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<GroupModel>>() {});
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void testGetGroupsOfSecretary() throws Exception {
        groupModel.setGroupId(groupService
                .addGroup(groupModel.getName(), groupModel.getSecretary(),
                groupModel.getGroupMembers()).getGroupId());

        MvcResult mvcResult = mvc.perform(get("/groups/secretary/" + secretary.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        List<GroupModel> expected = Arrays.asList(groupModel);
        List<GroupModel> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<GroupModel>>() {});
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void testGetGroupInfo() throws Exception {
        groupModel.setGroupId(groupService
                .addGroup(groupModel.getName(), groupModel.getSecretary(),
                groupModel.getGroupMembers()).getGroupId());

        MvcResult mvcResult = mvc.perform(get("/groups/" + groupModel.getGroupId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        GroupModel result = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<GroupModel>() {});
        assertEquals(groupModel, result);
    }
}