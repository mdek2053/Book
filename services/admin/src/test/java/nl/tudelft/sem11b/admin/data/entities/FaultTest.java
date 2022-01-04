package nl.tudelft.sem11b.admin.data.entities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FaultTest {
    private final transient Fault fault1 = new Fault(1L, "description", null);
    private final transient Fault fault2 = new Fault(1L, "description", null);

    @Test
    void equalsSameObjectTest() {
        assertTrue(fault1.equals(fault1));
    }

    @Test
    void equalsDifferentClassTest() {
        assertFalse(fault1.equals(" "));
    }

    @Test
    void equalsIdNotEqualTest() {
        fault2.setId(20L);
        assertFalse(fault1.equals(fault2));
    }

    @Test
    void equalsReporterNotEqualTest() {
        fault2.setReporter(4L);
        assertFalse(fault1.equals(fault2));
    }

    @Test
    void equalsDescriptionNotEqualTest() {
        fault2.setDescription("bla");
        assertFalse(fault1.equals(fault2));
    }

    @Test
    void equalsRoomNotEqualTest() {
        fault2.setRoom(new Room(0L, "suffix", "name", 10,
                null, null, new HashSet<>()));
        assertFalse(fault1.equals(fault2));
    }
    @Test
    void equalsSuccessfulTest() {
        assertTrue(fault1.equals(fault2));
    }
}
