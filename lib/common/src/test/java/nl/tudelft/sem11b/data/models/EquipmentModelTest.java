package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EquipmentModelTest {

    transient EquipmentModel model = new EquipmentModel(1L, "Projector");

    @Test
    void getId() {
        assertEquals(1L, model.getId());
    }

    @Test
    void getName() {
        assertEquals("Projector", model.getName());
    }

    @Test
    void setId() {
        model.setId(2L);
        assertEquals(2L, model.getId());
    }

    @Test
    void setName() {
        model.setName("Whiteboard");
        assertEquals("Whiteboard", model.getName());
    }
}