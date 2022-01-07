package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import nl.tudelft.sem11b.data.ApiDate;
import org.junit.jupiter.api.Test;

class RoomStudModelTest {

    RoomStudModel model = new RoomStudModel(1L, "suff", "room", 6);
    RoomStudModel model2 = new RoomStudModel(1L, "suff", "room", 6);

    @Test
    void setId() {
        model.setId(2L);
        assertEquals(2L, model.getId());
    }

    @Test
    void setSuffix() {
        model.setSuffix("suff2");
        assertEquals("suff2", model.getSuffix());
    }

    @Test
    void setName() {
        model.setName("room2");
        assertEquals("room2", model.getName());
    }

    @Test
    void setCapacity() {
        model.setCapacity(7);
        assertEquals(7, model.getCapacity());
    }

    @Test
    void setClosure() {
        ApiDate since = new ApiDate(2022, 10, 10);
        ApiDate until = new ApiDate(2022, 10, 12);
        ClosureModel closure = new ClosureModel("Room under maintenance.", since, until);
        model.setClosure(closure);
        assertEquals(closure, model.getClosure());
    }

    @Test
    void testSameEqual() {
        assertEquals(model, model);
    }

    @Test
    void testNotInstance() {
        assertFalse(model.equals(new Object()));
    }

    @Test
    void testEqualsNull() {
        assertFalse(model.equals(null));
    }

    @Test
    void testEqualsDifferentId() {
        model2.setId(12L);
        assertFalse(model.equals(model2));
    }

    @Test
    void testEqualsDifferentCapacity() {
        model2.setCapacity(12);
        assertFalse(model.equals(model2));
    }

    @Test
    void testEqualsDifferentSuffix() {
        model2.setSuffix("Chonk");
        assertFalse(model.equals(model2));
    }

    @Test
    void testEqualsDifferentName() {
        model2.setName("Chonk");
        assertFalse(model.equals(model2));
    }

    @Test
    void testEqualsDifferentClosure() {
        model2.setClosure(new ClosureModel("Bonk"));
        assertFalse(model.equals(model2));
    }

    @Test
    void testEqualsSuccessful() {
        assertTrue(model.equals(model2));
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hashCode(model), model.hashCode());
    }
}