package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Objects;

import nl.tudelft.sem11b.data.ApiDate;
import org.junit.jupiter.api.Test;

class RoomStudModelTest {

    RoomStudModel model = new RoomStudModel(1L, "suff", "room", 6);

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
    void testHashCode() {
        assertEquals(Objects.hashCode(model), model.hashCode());
    }
}