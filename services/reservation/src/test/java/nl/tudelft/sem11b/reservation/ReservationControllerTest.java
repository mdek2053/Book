package nl.tudelft.sem11b.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.ForbiddenException;
import nl.tudelft.sem11b.data.exception.NotFoundException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.reservation.entity.ReservationRequest;
import nl.tudelft.sem11b.services.ReservationService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ReservationService reservationService;

    @Test
    void makeReservationTest() throws Exception {
        when(reservationService.makeOwnReservation(1L, "foo", "Meeting",
                "2022-01-15T13:00", "2022-01-15T17:00")).thenReturn(1L);

        ReservationRequest req = new ReservationRequest(1L, "Meeting",
                "2022-01-15T13:00", "2022-01-15T17:00", null);

        MvcResult mvcResult = mockMvc.perform(post("/reservations")
                        .header("Authorization", "foo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject respObj = new JSONObject(response);
        assertTrue(respObj.has("id"));
        assertEquals(respObj.getLong("id"), 1L);
    }

    @Test
    void editReservationSuccessfully() throws Exception {
        long roomId = 111L;
        long userId = 123L;
        long reservationId = 222L;
        String token = "token123";
        String title = "Meeting";
        String since = "2022-01-15T13:00";
        String until = "2022-01-15T17:00";
        ReservationModel model = new ReservationModel(roomId, since, until, title);
        ReservationRequest req = new ReservationRequest(roomId, title, since, until, userId);
        when(reservationService.editReservation(token, model, reservationId))
                .thenReturn(reservationId);

        MvcResult mvcResult = mockMvc.perform(post("/reservations/222")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject respObj = new JSONObject(response);
        assertTrue(respObj.has("id"));
        assertEquals(respObj.getLong("id"), reservationId);
    }

}