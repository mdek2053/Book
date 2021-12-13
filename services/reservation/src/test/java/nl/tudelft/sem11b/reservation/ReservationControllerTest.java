package nl.tudelft.sem11b.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import nl.tudelft.sem11b.data.ApiDateTime;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
class ReservationControllerTest {
    private static final ObjectMapper mapper = new JsonMapper();
    private static final ReservationModel subject = new ReservationModel(
        1L,
        new ApiDateTime(2022, 1, 15, 13, 0),
        new ApiDateTime(2022, 1, 15, 17, 0),
        "Meeting"
    );

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ReservationService reservationService;

    @Test
    void makeReservationTest() throws Exception {
        final var resrv = 2L;

        // arrange
        when(reservationService.makeOwnReservation(
            subject.getRoomId(), subject.getTitle(), subject.getSince(), subject.getUntil())
        ).thenReturn(resrv);

        var req = new ReservationRequestModel(
            subject.getRoomId(), subject.getTitle(), subject.getSince(), subject.getUntil(), null);

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
        var page = mapper.readValue(response, new TypeReference<PageData<ReservationModel>>() {});
        assertEquals(Optional.of(subject), page.getData().findFirst());
    }

    @Test
    void editReservationSuccessfully() throws Exception {
        final var id = 2L;

        // arrange
        doNothing().when(reservationService).editReservation(
            id, subject.getTitle() + "!", subject.getSince(), subject.getUntil());
        var req = new ReservationRequestModel(
            subject.getRoomId(), subject.getTitle() + "!",
            subject.getSince(), subject.getUntil(), null);

        // action
        mockMvc.perform(post("/reservations/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(req)))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        // assert
        verify(reservationService, times(1)).editReservation(
            id, subject.getTitle() + "!", subject.getSince(), subject.getUntil());
    }

}
