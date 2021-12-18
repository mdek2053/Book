package nl.tudelft.sem11b.admin.data.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Fault;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.services.RoomsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

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
        MvcResult mvcResult = mockMvc.perform(post("/rooms/1/closure")
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
        MvcResult mvcResult = mockMvc.perform(delete("/rooms/1/closure"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // assert
        verify(roomsService, times(1)).reopenRoom(1);
    }

    @Test
    void listFaultsException() throws Exception {
        when(roomsService.listFaults(any())).thenThrow(new EntityNotFound("Fault"));

        mockMvc.perform(get("/faults")
                        .param("page", String.valueOf(1))
                        .param("limit", String.valueOf(1)))
                .andExpect(status().isNotFound())
                .andReturn();

        // assert
        verify(roomsService, times(1))
                .listFaults(PageIndex.fromQuery(Optional.of(1), Optional.of(1)));
    }

    @Test
    void listFaultsSuccess() throws Exception {
        mockMvc.perform(get("/faults")
                        .param("page", String.valueOf(1))
                        .param("limit", String.valueOf(1)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // assert
        verify(roomsService, times(1))
                .listFaults(PageIndex.fromQuery(Optional.of(1), Optional.of(1)));
    }

    @Test
    void listFaultsByRoomException() throws Exception {
        when(roomsService.listFaults(any(), anyLong()))
                .thenThrow(new EntityNotFound("Room"));

        mockMvc.perform(get("/rooms/1/faults")
                        .param("page", String.valueOf(1))
                        .param("limit", String.valueOf(1)))
                .andExpect(status().isNotFound())
                .andReturn();

        // assert
        verify(roomsService, times(1))
                .listFaults(PageIndex.fromQuery(Optional.of(1), Optional.of(1)), 1);
    }

    @Test
    void listFaultsByRoomSuccess() throws Exception {
        mockMvc.perform(get("/rooms/1/faults")
                        .param("page", String.valueOf(1))
                        .param("limit", String.valueOf(1)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // assert
        verify(roomsService, times(1))
                .listFaults(PageIndex.fromQuery(Optional.of(1), Optional.of(1)), 1);
    }

    @Test
    void addFaultException() throws Exception {
        ObjectMapper mapper = new JsonMapper();

        Room room = new Room(1L,"BOL", "Boole", 100,
                null,
                new Building(
                        1L, "EWI", "EEMCS building",
                        new ApiTime(8, 0), new ApiTime(22, 0),
                        Set.of()), Set.of());

        Fault fault = new Fault(1, "Blue ball machine broke", room);

        doThrow(new EntityNotFound("Room"))
                .when(roomsService)
                .addFault(1L, fault.toRequestModel());

        mockMvc.perform(post("/rooms/1/faults")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fault.toRequestModel())))
                .andExpect(status().isNotFound())
                .andReturn();

        // assert
        verify(roomsService, times(1))
                .addFault(1, fault.toRequestModel());
    }

    @Test
    void addFaultSuccess() throws Exception {
        ObjectMapper mapper = new JsonMapper();

        Room room = new Room(1L,"BOL", "Boole", 100,
                null,
                new Building(
                        1L, "EWI", "EEMCS building",
                        new ApiTime(8, 0), new ApiTime(22, 0),
                        Set.of()), Set.of());

        Fault fault = new Fault(1L, "Blue ball machine broke", room);

        mockMvc.perform(post("/rooms/1/faults")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fault.toRequestModel())))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // assert
        verify(roomsService, times(1))
                .addFault(1, fault.toRequestModel());
    }

    @Test
    void getFaultNoRoom() throws Exception {
        when(roomsService.getFault(1))
                .thenThrow(new ApiException("Rooms", "No room found"));

        mockMvc.perform(get("/faults/1"))
                .andExpect(status().isBadGateway())
                .andReturn();

        // assert
        verify(roomsService, times(1))
                .getFault(1);
    }

    @Test
    void getFaultNoFault() throws Exception {
        when(roomsService.getFault(1))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/faults/1"))
                .andExpect(status().isNotFound())
                .andReturn();

        // assert
        verify(roomsService, times(1))
                .getFault(1);
    }

    @Test
    void getFaultSuccess() throws Exception {
        ObjectMapper mapper = new JsonMapper();

        Room room = new Room(1L,"BOL", "Boole", 100,
                null,
                new Building(
                        1L, "EWI", "EEMCS building",
                        new ApiTime(8, 0), new ApiTime(22, 0),
                        Set.of()), Set.of());

        Fault fault = new Fault(1L, "Blue ball machine broke", room);

        when(roomsService.getFault(1))
                .thenReturn(Optional.of(fault.toModel()));

        MvcResult mvcResult = mockMvc.perform(get("/faults/1"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // assert
        verify(roomsService, times(1))
                .getFault(1);
        assertEquals(mapper
                        .readValue(mvcResult.getResponse().getContentAsString(),
                        FaultModel.class),
                fault.toModel());
    }

    @Test
    void resolveFault() throws Exception {
        doNothing().when(roomsService).resolveFault(1);

        // action
        mockMvc.perform(delete("/faults/1"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // assert
        verify(roomsService, times(1)).resolveFault(1);
    }

}