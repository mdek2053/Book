package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Objects;

import org.junit.jupiter.api.Test;

class FaultRequestModelTest {

    FaultRequestModel model = new FaultRequestModel(1L, "Valid description.");

    @Test
    void testNotNull() {
        FaultRequestModel notNull = new FaultRequestModel();
        assertNotNull(notNull);
    }

    @Test
    void getReservationId() {
        assertEquals(1L, model.getReservationId());
    }

    @Test
    void getDescription() {
        assertEquals("Valid description.", model.getDescription());
    }

    @Test
    void setReservationId() {
        model.setReservationId(2L);
        assertEquals(2L, model.getReservationId());
    }

    @Test
    void setDescription() {
        model.setDescription("Different description.");
        assertEquals("Different description.", model.getDescription());
    }

    @Test
    void testTheSame() {
        assertEquals(model, model);
    }

    @Test
    void testEqual() {
        FaultRequestModel same = new FaultRequestModel(1L, "Valid description.");
        assertEquals(same, model);
    }

    @Test
    void testNotInstanceOf() {
        assertFalse(model.equals(new Object()));
    }

    @Test
    void testNotEqualReservationId() {
        FaultRequestModel diff = new FaultRequestModel(2L, "Valid description.");
        assertNotEquals(diff, model);
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hashCode(model), model.hashCode());
    }
}