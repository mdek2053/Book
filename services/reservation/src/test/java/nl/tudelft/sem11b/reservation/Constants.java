package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.EquipmentModel;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.UserModel;

public class Constants {
    public static final RoomModel ROOM_A = new RoomModel(
        1L, "BOL", "Boole", 100,
        new BuildingModel(
            1L, "EWI", "EEMCS building",
            new ApiTime(8, 0), new ApiTime(22, 0)),
        new EquipmentModel[]{new EquipmentModel(1L, "Whiteboard")}
    );
    public static final RoomModel ROOM_B = new RoomModel(
        2L, "TZ2", "Boole", 250,
        new BuildingModel(
            2L, "DBW", "Drebbelweg",
            new ApiTime(8, 0), new ApiTime(22, 0)),
        new EquipmentModel[0]
    );
    public static final UserModel USER_A = new UserModel(1L, "jgeneric", new String[0]);
    public static final UserModel USER_B = new UserModel(2L, "jsmith", new String[0]);
}
