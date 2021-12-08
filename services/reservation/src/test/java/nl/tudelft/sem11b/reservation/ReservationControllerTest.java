package nl.tudelft.sem11b.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.reservation.entity.ReservationRequest;
import nl.tudelft.sem11b.services.ReservationService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    void inspectOwnReservation() throws Exception {
        ReservationModel reservationModel1 = new ReservationModel(1L,
                Timestamp.valueOf("2022-01-15 13:00:00"), Timestamp.valueOf("2022-01-15 17:00:00"), "Meeting");

        List<ReservationModel> reservationModelList = new ArrayList<>();
        reservationModelList.add(reservationModel1);

        when(reservationService.inspectOwnReservation("token")).thenReturn(reservationModelList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/reservations/mine")
                .header("Authorization", "token")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"roomId\":1,\"since\":\"2022-01-15T12:00:00.000+00:00\"," +
                "\"until\":\"2022-01-15T16:00:00.000+00:00\",\"title\":\"Meeting\"}]";
        assertEquals(expected, result.getResponse().getContentAsString());
    }

}
