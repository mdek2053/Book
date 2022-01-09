package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class EquipmentModelTest {

    transient EquipmentModel model = new EquipmentModel(1L, "Projector");
    transient EquipmentModel model2 = new EquipmentModel(1L, "Projector");

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

    @Test
    void equalsNullTest() {
        assertFalse(model.equals(null));
    }

    @Test
    void equalsDifferentClassTest() {
        assertFalse(model.equals(" "));
    }

    @Test
    void equalsDifferentIdTest() {
        model2.setId(12L);
        assertFalse(model.equals(model2));
    }

    @Test
    void equalsDifferentNameTest() {
        model2.setName("Beamer");
        assertFalse(model.equals(model2));
    }


    @Test
    void equalsSuccessfulTest() {
        assertEquals(model, model2);
    }
}