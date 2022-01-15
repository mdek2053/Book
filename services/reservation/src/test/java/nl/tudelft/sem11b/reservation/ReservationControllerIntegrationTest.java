package nl.tudelft.sem11b.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.services.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;


@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
class ReservationControllerIntegrationTest {
    private static final ObjectMapper mapper = new JsonMapper();
    private static final ReservationModel subject = new ReservationModel(
        1L,
        new ApiDateTime(new ApiDate(2022, 1, 15), new ApiTime(13, 0)),
        new ApiDateTime(new ApiDate(2022, 1, 15), new ApiTime(17, 0)),
        "Meeting"
    );

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ReservationService reservationService;

    @Test
    void makeReservationTest() throws Exception {
        final var resrv = 2L;
        var req = new ReservationRequestModel(
                subject.getRoomId(), subject.getTitle(),
                subject.getSince(), subject.getUntil(), null);

        // arrange
        when(reservationService.makeOwnReservation(
            req)
        ).thenReturn(resrv);

        // action
        MvcResult mvcResult = mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        // assert
        var id = mapper.readValue(response, new TypeReference<IdModel<Long>>() {});
        assertEquals(resrv, id.getId());
    }

    @Test
    void makeReservationForUserTest() throws Exception {
        final var resrv = 2L;
        final long userId = 11L;
        var req = new ReservationRequestModel(
                subject.getRoomId(), subject.getTitle(),
                subject.getSince(), subject.getUntil(), userId);

        // arrange
        when(reservationService.makeUserReservation(req)
        ).thenReturn(resrv);

        // action
        MvcResult mvcResult = mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        // assert
        var id = mapper.readValue(response, new TypeReference<IdModel<Long>>() {});
        assertEquals(resrv, id.getId());
    }

    @Test
    void makeReservationEmptyRequest() throws Exception {
        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new ReservationRequestModel())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void makeReservationNullFields() throws Exception {
        var req = new ReservationRequestModel(
                null, subject.getTitle(),
                subject.getSince(), subject.getUntil(), null);
        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void makeReservationForUserThrowException() throws Exception {
        final long userId = 11L;
        var req = new ReservationRequestModel(
                subject.getRoomId(), subject.getTitle(),
                subject.getSince(), subject.getUntil(), userId);
        when(reservationService.makeUserReservation(req)
        ).thenThrow(new EntityNotFound("Room"));

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void makeOwnReservationThrowException() throws Exception {
        var req = new ReservationRequestModel(
                subject.getRoomId(), subject.getTitle(),
                subject.getSince(), subject.getUntil(), null);
        when(reservationService.makeOwnReservation(req)
        ).thenThrow(new EntityNotFound("Room"));

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void inspectOwnReservation() throws Exception {
        final var reservations = new ArrayList<ReservationModel>();
        reservations.add(subject);

        // arrange
        when(reservationService.inspectOwnReservation(new PageIndex(1, 3)))
            .thenReturn(new PageData<>(1, reservations));

        // action
        MvcResult mvcResult = mockMvc.perform(get("/reservations/mine?page=1&limit=3")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        // assert
        var page = mapper.readValue(response,
                new TypeReference<PageData<ReservationModel>>() {});
        assertEquals(Optional.of(subject), page.getData().findFirst());
    }

    @Test
    void inspectOwnReservationThrowException() throws Exception {
        final var reservations = new ArrayList<ReservationModel>();
        reservations.add(subject);

        // arrange
        when(reservationService.inspectOwnReservation(new PageIndex(1, 3)))
                .thenThrow(new ApiException("users", "user not authenticated"));

        // action
        mockMvc.perform(get("/reservations/mine?page=1&limit=3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void checkAvailabilityTest() throws Exception {
        var req = new ReservationRequestModel(
                subject.getRoomId(), subject.getTitle(),
                subject.getSince(), subject.getUntil(), null);
        when(reservationService.checkAvailability(subject.getRoomId(), req))
                .thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(
                post("/reservations/availability/" + subject.getRoomId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(mapper.readValue(response, new TypeReference<Boolean>() {}));
    }

    @Test
    void checkAvailabilityInvalidDataTest() throws Exception {
        var req = new ReservationRequestModel(
                subject.getRoomId(), subject.getTitle(),
                subject.getSince(), subject.getUntil(), null);
        when(reservationService.checkAvailability(subject.getRoomId(), req))
                .thenThrow(new InvalidData("Invalid room id"));

        MvcResult mvcResult = mockMvc.perform(post("/reservations/availability/" + req.getRoomId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(!mapper.readValue(response, new TypeReference<Boolean>() {}));
    }

    @Test
    void editReservationSuccessfully() throws Exception {
        final var id = 2L;
        var req = new ReservationRequestModel(
                subject.getRoomId(), subject.getTitle() + "!",
                subject.getSince(), subject.getUntil(), null);
        // arrange
        doNothing().when(reservationService).editReservation(
            id, req);

        // action
        mockMvc.perform(post("/reservations/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        // assert
        verify(reservationService, times(1)).editReservation(
            id, req);
    }

    @Test
    void changeReservationEmptyRequest() throws Exception {
        ReservationController sut = new ReservationController(reservationService);
        assertThrows(ResponseStatusException.class, () -> sut.changeReservation(1L, null));
    }

    @Test
    void changeReservationInvalidData() throws ApiException, EntityNotFound, InvalidData {
        ReservationController sut = new ReservationController(reservationService);
        final var id = 2L;
        var req = new ReservationRequestModel(
                subject.getRoomId(), subject.getTitle(),
                subject.getSince(), subject.getUntil(), null);

        // arrange
        doThrow(new EntityNotFound("Room")).when(reservationService).editReservation(
                id, req);

        // assert
        assertThrows(ResponseStatusException.class, () -> sut.changeReservation(id, req));
    }

    @Test
    void deleteReservationSuccessfully() throws Exception {
        final var id = 2L;
        doNothing().when(reservationService).deleteReservation(id);

        mockMvc.perform(delete("/reservations/" + id))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(reservationService, times(1)).deleteReservation(id);
    }

    @Test
    void deleteReservationInvalidData() throws Exception {
        final var id = 2L;
        doThrow(new EntityNotFound("Reservation"))
                .when(reservationService).deleteReservation(id);

        mockMvc.perform(delete("/reservations/" + id))
                .andExpect(status().is4xxClientError());

        verify(reservationService, times(1)).deleteReservation(id);
    }
}
