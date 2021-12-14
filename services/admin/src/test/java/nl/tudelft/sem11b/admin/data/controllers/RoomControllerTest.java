package nl.tudelft.sem11b.admin.data.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.services.RoomsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(RoomController.class)
class RoomControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RoomsService roomsService;

    RoomController controller;

    @BeforeEach
    void initController() {
        controller = new RoomController(roomsService);
    }

    @Test
    void closeRoom() throws Exception {
        // arrange
        ObjectMapper mapper = new JsonMapper();
        Closure closure = new Closure("blue ball machine broke",
                new ApiDate(2021, 9, 1),
                new ApiDate(2021, 9, 13));

        // action
        MvcResult mvcResult = mockMvc.perform(post("/room/1/closure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(closure.toModel())))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // assert
        verify(roomsService, times(1)).closeRoom(1, closure.toModel());
    }

    @Test
    void reopenRoom() throws Exception {
        // action
        MvcResult mvcResult = mockMvc.perform(delete("/room/1/closure"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // assert
        verify(roomsService, times(1)).reopenRoom(1);
    }

}