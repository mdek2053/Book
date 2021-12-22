package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiTime;
import org.junit.jupiter.api.Test;

class RoomModelTest {

    ApiTime start = new ApiTime(9, 0);
    ApiTime end = new ApiTime(21, 0);
    BuildingModel buildingModel =
            new BuildingModel(1L, "pref", "EWI", start, end);
    EquipmentModel equipmentModel = new EquipmentModel(1L, "Projector");
    RoomModel model =
            new RoomModel(1L, "suff", "room", 6,
                    buildingModel, new EquipmentModel[]{equipmentModel});

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
    void setBuilding() {
        BuildingModel buildingModel2 = new BuildingModel(2L, "pref", "Pulse", start, end);
        model.setBuilding(buildingModel2);
        assertEquals(buildingModel2, model.getBuilding());
    }

    @Test
    void setEquipment() {
        EquipmentModel[] newArr = new EquipmentModel[]{new EquipmentModel(2L, "Whiteboard")};
        model.setEquipment(newArr);
        List<EquipmentModel> result = model.getEquipment().collect(Collectors.toList());
        List<EquipmentModel> expected = Arrays.asList(newArr);
        assertEquals(expected, result);
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