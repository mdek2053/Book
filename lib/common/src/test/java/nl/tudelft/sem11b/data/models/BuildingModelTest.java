package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Objects;

import nl.tudelft.sem11b.data.ApiTime;
import org.junit.jupiter.api.Test;

class BuildingModelTest {

    transient ApiTime start = new ApiTime(9, 0);
    transient ApiTime end = new ApiTime(21, 0);
    transient BuildingModel model1 = new BuildingModel(1L, "pref", "EWI", start, end);

    @Test
    void getId() {
        assertEquals(1L, model1.getId());
    }

    @Test
    void getPrefix() {
        assertEquals("pref", model1.getPrefix());
    }

    @Test
    void getName() {
        assertEquals("EWI", model1.getName());
    }

    @Test
    void getOpen() {
        assertEquals(start, model1.getOpen());
    }

    @Test
    void getClose() {
        assertEquals(end, model1.getClose());
    }

    @Test
    void setId() {
        model1.setId(2L);
        assertEquals(2L, model1.getId());
    }

    @Test
    void setPrefix() {
        model1.setPrefix("pref2");
        assertEquals("pref2", model1.getPrefix());
    }

    @Test
    void setName() {
        model1.setName("Pulse");
        assertEquals("Pulse", model1.getName());
    }

    @Test
    void setOpen() {
        model1.setOpen(new ApiTime(8, 0));
        assertEquals(new ApiTime(8, 0), model1.getOpen());
    }

    @Test
    void setClose() {
        model1.setClose(new ApiTime(21, 30));
        assertEquals(new ApiTime(21, 30), model1.getClose());
    }

    @Test
    void testEquals() {
        BuildingModel same = new BuildingModel(1L, "pref", "EWI", start, end);
        assertEquals(same, model1);
    }

    @Test
    void testEqualsTheSame() {
        assertEquals(model1, model1);
    }

    @Test
    void testNotEquals() {
        assertFalse(model1.equals(null));
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hash(1L, "pref", "EWI", start, end),
                model1.hashCode());
    }
}