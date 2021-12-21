package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Objects;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiTime;
import org.junit.jupiter.api.Test;

class ClosureModelTest {

    transient ApiDate since = new ApiDate(2022, 10, 10);
    transient ApiDate until = new ApiDate(2022, 10, 12);
    transient ClosureModel model = new ClosureModel("Room under maintenance.", since, until);

    @Test
    void testNotNull() {
        ClosureModel notNull = new ClosureModel("Valid reason.");
        assertNotNull(notNull);
    }

    @Test
    void getReason() {
        assertEquals("Room under maintenance.", model.getReason());
    }

    @Test
    void getSince() {
        assertEquals(since, model.getSince());
    }

    @Test
    void getUntil() {
        assertEquals(until, model.getUntil());
    }

    @Test
    void setReason() {
        model.setReason("A new reason.");
        assertEquals("A new reason.", model.getReason());
    }

    @Test
    void setSince() {
        model.setSince(new ApiDate(2022, 10, 9));
        assertEquals(new ApiDate(2022, 10, 9), model.getSince());
    }

    @Test
    void setUntil() {
        model.setUntil(new ApiDate(2022, 11, 10));
        assertEquals(new ApiDate(2022, 11, 10), model.getUntil());
    }

    @Test
    void testEquals() {
        ClosureModel same = new ClosureModel("Room under maintenance.", since, until);
        assertEquals(same, model);
    }

    @Test
    void testTheSame() {
        assertEquals(model, model);
    }

    @Test
    void testNull() {
        assertNotEquals(null, model);
    }

    @Test
    void testNotEqualReason() {
        assertNotEquals(new ClosureModel("Different reason.", since, until), model);
    }

    @Test
    void testNotEqualSince() {
        assertNotEquals(new ClosureModel("Room under maintenance.",
                new ApiDate(2023, 1), until),
                model);
    }

    @Test
    void testNotInstance() {
        assertFalse(model.equals(new Object()));
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hashCode(model), model.hashCode());
    }
}