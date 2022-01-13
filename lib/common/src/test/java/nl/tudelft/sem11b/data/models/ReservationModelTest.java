package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.ApiTime;
import org.junit.jupiter.api.Test;

public class ReservationModelTest {

    private final transient ApiDateTime since =
            new ApiDateTime(new ApiDate(2022, 1, 1), new ApiTime(10L, 0L));
    private final transient ApiDateTime until =
            new ApiDateTime(new ApiDate(2022, 1, 1), new ApiTime(11L, 0L));
    private final transient ReservationModel model =
            new ReservationModel(1L, since, until, "Meeting");
    private final transient ReservationModel model2 =
            new ReservationModel(1L, since, until, "Meeting");

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
    void equalsSuccessfulTest() {
        assertTrue(model.equals(model2));
    }
}
