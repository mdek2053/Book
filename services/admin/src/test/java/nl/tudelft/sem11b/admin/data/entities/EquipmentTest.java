package nl.tudelft.sem11b.admin.data.entities;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EquipmentTest {

    private final transient Equipment equipment1 = new Equipment(0L, "beamer");
    private final transient Equipment equipment2 = new Equipment(0L, "beamer");

    @Test
    void equalsNullTest() {
        assertFalse(equipment1.equals(null));
    }

    @Test
    void equalsDifferentClassTest() {
        assertFalse(equipment1.equals(" "));
    }

    @Test
    void equalsIdNotEqualTest() {
        equipment2.setId(1L);
        assertFalse(equipment1.equals(equipment2));
    }

    @Test
    void equalsNameNotEqualTest() {
        equipment2.setName("blue ball machine");
        assertFalse(equipment1.equals(equipment2));
    }

    @Test
    void equalsSuccessfulTest() {
        assertTrue(equipment1.equals(equipment2));
    }


}
