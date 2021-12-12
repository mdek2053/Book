package nl.tudelft.sem11b.admin.data.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem11b.admin.services.ServerInteractionHelper;
import nl.tudelft.sem11b.data.models.ClosureObject;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.services.RoomsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(RoomController.class)
class RoomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RoomController roomController;

    @MockBean
    RoomsService rooms;

    @MockBean
    ServerInteractionHelper helper;

    @Test
    void closeRoom() throws Exception {
        roomController.setServerInteractionHelper(helper);

        ClosureObject closure = new ClosureObject("a banana fell",
                "2021-09-20", "2021-09-21");

        doNothing().when(helper).verifyUserAdmin("foo");
        when(rooms.getRoom(1)).thenReturn(Optional.of(new RoomModel(1,
                "", "", 1, null)));
        doNothing().when(rooms).closeRoom(1, closure);

        MvcResult mvcResult = mockMvc.perform(post("/room/1/closure")
                        .header("Authorization", "foo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(closure)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void reopenRoom() throws Exception {
        roomController.setServerInteractionHelper(helper);

        doNothing().when(helper).verifyUserAdmin("foo");
        when(rooms.getRoom(1)).thenReturn(Optional.of(new RoomModel(1,
                "", "", 1, null)));
        doNothing().when(rooms).reopenRoom(1);

        MvcResult mvcResult = mockMvc.perform(delete("/room/1/closure")
                        .header("Authorization", "foo"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }
}