package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.ApiTime;
import org.junit.jupiter.api.Test;

public class ReservationRequestModelTest {

    private final transient ApiDateTime since =
            new ApiDateTime(new ApiDate(2022, 1, 1), new ApiTime(10L, 0L));
    private final transient ApiDateTime until =
            new ApiDateTime(new ApiDate(2022, 1, 1), new ApiTime(11L, 0L));
    private final transient ReservationRequestModel model =
            new ReservationRequestModel(1L, "Meeting", since, until, 1L);
    private final transient ReservationRequestModel model2 =
            new ReservationRequestModel(1L, "Meeting", since, until, 1L);

    @Test
    void validateRoomIdNullTest() {
        model.setRoomId(null);
        assertFalse(model.validate());
    }

    @Test
    void validateTitleNullTest() {
        model.setTitle(null);
        assertFalse(model.validate());
    }

    @Test
    void validateTitleBlankTest() {
        model.setTitle("");
        assertFalse(model.validate());
    }

    @Test
    void validateSinceNullTest() {
        model.setSince((ApiDateTime) null);
        assertFalse(model.validate());
    }

    @Test
    void validateUntilNullTest() {
        model.setUntil((ApiDateTime) null);
        assertFalse(model.validate());
    }

    @Test
    void validateSuccessfulTest() {
        assertTrue(model.validate());
    }

    @Test
    void equalsSameObjectTest() {
        assertTrue(model.equals(model));
    }

    @Test
    void equalsNullTest() {
        assertFalse(model.equals(null));
    }

    @Test
    void equalsDifferentClassTest() {
        assertFalse(model.equals(" "));
    }

    @Test
    void equalsDifferentRoomIdTest() {
        model2.setRoomId(2L);
        assertFalse(model.equals(model2));
    }

    @Test
    void equalsDifferentTitleTest() {
        model2.setTitle("bla");
        assertFalse(model.equals(model2));
    }

    @Test
    void equalsDifferentSinceTest() {
        model2.setSince(until);
        assertFalse(model.equals(model2));
    }

    @Test
    void equalsDifferentUntilTest() {
        model2.setUntil(since);
        assertFalse(model.equals(model2));
    }

    @Test
    void equalsDifferentForUserTest() {
        model2.setForUser(2L);
        assertFalse(model.equals(model2));
    }

    @Test
    void equalsSuccessfulTest() {
        assertTrue(model.equals(model2));
    }
}
