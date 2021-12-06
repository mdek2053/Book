package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.data.models.BuildingObject;
import nl.tudelft.sem11b.data.models.ClosureObject;
import nl.tudelft.sem11b.data.models.EquipmentObject;
import nl.tudelft.sem11b.data.models.RoomObject;

public class Constants {
    public static RoomObject full = new RoomObject(1L,
            "LH-AMP",
            "Lecture Hall Ampére",
            new BuildingObject(64L, "EWI",
                    "Faculty building of Electronic Engineering, Mathematics and Computer Science",
                    "07:00", "20:00"),
            329,
            new EquipmentObject[]{
                new EquipmentObject(35L, "Desktop PC"),
                new EquipmentObject(78L, "Smartboard")
            },
            new ClosureObject("Maintenance", "2022-06-12", "2022-06-20"));

    public static RoomObject noClosure = new RoomObject(1L,
            "LH-AMP",
            "Lecture Hall Ampére",
            new BuildingObject(64L, "EWI",
                    "Faculty building of Electronic Engineering, Mathematics and Computer Science",
                    "07:00", "20:00"),
            329,
            new EquipmentObject[]{
                new EquipmentObject(35L, "Desktop PC"),
                new EquipmentObject(78L, "Smartboard")
            },
            null);
}
