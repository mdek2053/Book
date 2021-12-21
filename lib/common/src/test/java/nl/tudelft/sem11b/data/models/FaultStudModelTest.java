package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FaultStudModelTest {

    transient long id = 1L;
    transient long userId = 222L;
    FaultStudModel model = new FaultStudModel(id, userId, "Valid description.");

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

}