package nl.tudelft.sem11b.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem11b.reservation.entity.ReservationRequest;
import nl.tudelft.sem11b.services.ReservationService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
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

}