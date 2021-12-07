package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.EquipmentModel;
import nl.tudelft.sem11b.data.models.RoomModel;

public class Constants {
    public static RoomModel full = new RoomModel(1,
        "LH-AMP",
        "Lecture Hall Ampére",
        329,
        new BuildingModel(64, "EWI",
            "Faculty building of Electronic Engineering, Mathematics and Computer Science",
            new ApiTime(7, 0), new ApiTime(20, 0)),
        new EquipmentModel[]{
            new EquipmentModel(35, "Desktop PC"),
            new EquipmentModel(78, "Smartboard")
        },
        new ClosureModel("Maintenance", new ApiDate(2022, 6, 12), new ApiDate(2022, 6, 20)));

    public static RoomModel noClosure = new RoomModel(1,
        "LH-AMP",
        "Lecture Hall Ampére",
        329,
        new BuildingModel(64, "EWI",
            "Faculty building of Electronic Engineering, Mathematics and Computer Science",
            new ApiTime(7, 0), new ApiTime(20, 0)),
        new EquipmentModel[]{
            new EquipmentModel(35, "Desktop PC"),
            new EquipmentModel(78, "Smartboard")
        });
}
