package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Objects;

import org.junit.jupiter.api.Test;

class FaultModelTest {

    transient long id = 1L;
    transient long userId = 222L;
    transient long roomId = 333L;
    transient FaultModel model = new FaultModel(id, userId, "Valid description.", roomId);

    @Test
    void getId() {
        assertEquals(id, model.getId());
    }

    @Test
    void getReporter() {
        assertEquals(userId, model.getReporter());
    }

    @Test
    void getDescription() {
        assertEquals("Valid description.", model.getDescription());
    }

    @Test
    void getRoomId() {
        assertEquals(roomId, model.getRoomId());
    }

    @Test
    void setId() {
        model.setId(2L);
        assertEquals(2L, model.getId());
    }

    @Test
    void setReporter() {
        model.setReporter(111L);
        assertEquals(111L, model.getReporter());
    }

    @Test
    void setDescription() {
        model.setDescription("Different description.");
        assertEquals("Different description.", model.getDescription());
    }

    @Test
    void setRoomId() {
        model.setRoomId(444L);
        assertEquals(444L, model.getRoomId());
    }

    @Test
    void testTheSame() {
        assertEquals(model, model);
    }

    @Test
    void testEqual() {
        FaultModel same = new FaultModel(id, userId, "Valid description.", roomId);
        assertEquals(same, model);
    }

    @Test
    void testNotInstanceOf() {
        assertFalse(model.equals(new Object()));
    }

    @Test
    void testNotEqualId() {
        FaultModel diff = new FaultModel(2L, userId, "Valid description.", roomId);
        assertNotEquals(diff, model);
    }

    @Test
    void testNotEqualReporter() {
        FaultModel diff = new FaultModel(id, 111L, "Valid description.", roomId);
        assertNotEquals(diff, model);
    }

    @Test
    void testNotEqualRoom() {
        FaultModel diff = new FaultModel(id, userId, "Valid description.", 111L);
        assertNotEquals(diff, model);
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hashCode(model), model.hashCode());
    }
}