package nl.tudelft.sem11b.admin.data.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.models.EquipmentModel;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import org.junit.jupiter.api.Test;

public class RoomTest {

    private final transient Room room = new Room();
    private final transient Closure closure = new Closure("reason", new ApiDate(2022, 1, 1));
    private final transient Building building = new Building();
    private final transient Room room2 = new Room(0L,
            "suffix", "name", 10, closure, building, new HashSet<>());
    private final transient Room room3 = new Room(0L,
            "suffix", "name", 10, closure, building, new HashSet<>());

    private final transient ApiTime opening = new ApiTime(10L, 0L);
    private final transient ApiTime closing = new ApiTime(22L, 0L);

    @Test
    void setSuffixNullTest() {
        assertThrows(IllegalArgumentException.class, () -> room.setSuffix(null));
    }

    @Test
    void setSuffixEmptyTest() {
        assertThrows(IllegalArgumentException.class, () -> room.setSuffix(" "));
    }

    @Test
    void setSuffixSuccessfulTest() {
        String suffix = "suffix";

        room.setSuffix(suffix);

        assertEquals(suffix, room.getSuffix());
    }

    @Test
    void setNameNullTest() {
        assertThrows(IllegalArgumentException.class, () -> room.setName(null));
    }

    @Test
    void setNameEmptyTest() {
        assertThrows(IllegalArgumentException.class, () -> room.setName(" "));
    }

    @Test
    void setNameSuccessfulTest() {
        String name = "name";

        room.setName(name);

        assertEquals(name, room.getName());
    }

    @Test
    void equalsNullTest() {
        assertFalse(room.equals(null));
    }

    @Test
    void equalsDifferentClassTest() {
        assertFalse(room.equals(" "));
    }

    @Test
    void equalsIdNotEqualTest() {
        room3.setId(1L);

        assertFalse(room2.equals(room3));
    }

    @Test
    void equalsCapacityNotEqualTest() {
        room3.setCapacity(20);

        assertFalse(room2.equals(room3));
    }

    @Test
    void equalsSuffixNotEqualTest() {
        room3.setSuffix("nope");

        assertFalse(room2.equals(room3));
    }

    @Test
    void equalsNameNotEqualTest() {
        room3.setName("nope");

        assertFalse(room2.equals(room3));
    }

    @Test
    void equalsClosureNotEqualTest() {
        room3.setClosure(new Closure("different reason", new ApiDate(2022, 1, 1)));

        assertFalse(room2.equals(room3));
    }

    @Test
    void equalsBuildingNotEqualTest() {
        room3.setBuilding(new Building(0L, "prefix", "name",
                opening, closing, new HashSet<Room>()));

        assertFalse(room2.equals(room3));
    }

    @Test
    void equalsEquipmentNotEqualTest() {
        room3.addEquipment(new Equipment());

        assertFalse(room2.equals(room3));
    }

    @Test
    void equalsSuccessfulTest() {
        assertTrue(room2.equals(room3));
    }

    @Test
    void toStudModelNullClosureTest() {
        room2.setClosure(null);
        assertEquals(new RoomStudModel(0L, "suffix", "name", 10),
                room2.toStudModel());
    }

    @Test
    void toStudModelNonNullClosureTest() {
        assertEquals(new RoomStudModel(0L, "suffix",
                        "name", 10, closure.toModel()),
                room2.toStudModel());
    }

    @Test
    void toModelNullClosureTest() {
        room2.setClosure(null);

        assertEquals(new RoomModel(0L, "suffix",
                "name", 10, building.toModel(), new EquipmentModel[0]),
                room2.toModel());
    }

    @Test
    void toModelNonNullClosureWithEquipmentTest() {
        Equipment equipment = new Equipment(1L, "beamer");
        room2.addEquipment(equipment);
        EquipmentModel[] models = new EquipmentModel[1];
        models[0] = equipment.toModel();

        assertEquals(new RoomModel(0L, "suffix",
                "name", 10, building.toModel(), models, closure.toModel()),
                room2.toModel());
    }
}
