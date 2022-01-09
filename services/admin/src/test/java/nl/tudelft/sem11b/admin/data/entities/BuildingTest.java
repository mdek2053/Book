package nl.tudelft.sem11b.admin.data.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.models.BuildingModel;
import org.junit.jupiter.api.Test;




public class BuildingTest {

    private final transient Building building = new Building();
    private final transient ApiTime opening = new ApiTime(10L, 0L);
    private final transient ApiTime openingPlus1 = new ApiTime(10L, 1L);
    private final transient ApiTime closing = new ApiTime(22L, 0L);

    private final transient Building building2 =
            new Building(0L, "prefix", "name", opening, closing, new HashSet<Room>());
    private final transient Building building3 =
            new Building(0L, "prefix", "name", opening, closing, new HashSet<Room>());
    private final transient BuildingModel building2Model =
            new BuildingModel(0L, "prefix", "name", opening, closing);

    private final transient Room room = new Room();


    @Test
    void setPrefixNullTest() {
        assertThrows(IllegalArgumentException.class, () -> building.setPrefix(null));
    }

    @Test
    void setPrefixEmptyTest() {
        assertThrows(IllegalArgumentException.class, () -> building.setPrefix(" "));
    }

    @Test
    void setPrefixSuccessfulTest() {
        String prefix = "Test";

        building.setPrefix(prefix);

        assertEquals(prefix, building.getPrefix());
    }

    @Test
    void setNameNullTest() {
        assertThrows(IllegalArgumentException.class, () -> building.setName(null));
    }

    @Test
    void setNameEmptyTest() {
        assertThrows(IllegalArgumentException.class, () -> building.setName(" "));
    }

    @Test
    void setNameSuccessfulTest() {
        String name = "Test";

        building.setName(name);

        assertEquals(name, building.getName());
    }

    @Test
    void setOpeningTest() {
        building.setHours(new ApiTime(1L, 0L), closing);
        building.setOpening(opening);
        assertEquals(building.getOpening(), opening);
    }

    @Test
    void setClosingTest() {
        //To avoid a nullpointer
        building.setHours(opening, new ApiTime(23L, 0L));
        building.setClosing(closing);
        assertEquals(building.getClosing(), closing);
    }

    @Test
    void setHoursClosesBeforeOpensTest() {
        assertThrows(IllegalArgumentException.class, () -> building.setHours(closing, opening));
    }

    @Test
    void setHoursClosesAndOpensSameTimeTest() {
        assertThrows(IllegalArgumentException.class, () -> building.setHours(opening, opening));
    }

    @Test
    void setHoursOpensImmediatelyClosesTest() {
        building.setHours(opening, openingPlus1);

        assertEquals(opening, building.getOpening());
        assertEquals(openingPlus1, building.getClosing());
    }

    @Test
    void setHoursSuccessfulTest() {
        building.setHours(opening, closing);

        assertEquals(opening, building.getOpening());
        assertEquals(closing, building.getClosing());
    }

    @Test
    void addRoomTest() {
        building2.addRoom(room);

        assertTrue(building2.getRooms().collect(Collectors.toSet()).contains(room));
    }

    @Test
    void constructorTest() {
        assertNotNull(new Building(0, "prefix", "name", opening, closing, Set.of()));
    }

    @Test
    void constructorTestwithoutId() {
        assertNotNull(new Building("prefix", "name", opening, closing, Set.of()));
    }

    @Test
    void toModelTest() {
        assertEquals(building2Model, building2.toModel());
    }

    @Test
    void equalsSameObjectTest() {
        assertTrue(building2.equals(building2));
    }

    @Test
    void equalsNullTest() {
        assertFalse(building2.equals(null));
    }

    @Test
    void equalsDifferentClassTest() {
        assertFalse(building2.equals(building2Model));
    }

    @Test
    void equalsTrueTest() {
        assertTrue(building2.equals(
                new Building(building2.getId(), building2.getPrefix(),
                        building2.getName(), building2.getOpening(),
                        building2.getClosing(), building2.getRooms().collect(Collectors.toSet()))));
    }

    @Test
    void equalsIdNotEqualTest() {
        building3.setId(2L);
        assertFalse(building2.equals(building3));

    }

    @Test
    void equalsPrefixNotEqualTest() {
        building3.setPrefix("no");
        assertFalse(building2.equals(building3));

    }

    @Test
    void equalsNameNotEqualTest() {
        building3.setName("no");
        assertFalse(building2.equals(building3));

    }

    @Test
    void equalsOpeningNotEqualTest() {
        building3.setOpening(openingPlus1);
        assertFalse(building2.equals(building3));

    }

    @Test
    void equalsClosingNotEqualTest() {
        building3.setClosing(openingPlus1);
        assertFalse(building2.equals(building3));

    }

    @Test
    void equalsRoomsNotEqualTest() {
        building3.addRoom(room);
        assertFalse(building2.equals(building3));

    }

    @Test
    void hashCodeTest() {
        assertNotEquals(building.hashCode(), building2.hashCode());
    }

    @Test
    void toStringTest() {
        String s = "Building{id=0, prefix='prefix', "
                + "name='name', opening=10:00, closing=22:00, rooms=[]}";
        assertEquals(s, building2.toString());
    }
}
