package nl.tudelft.sem11b.services;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;

public interface RoomsService {
    PageData<RoomStudModel> listRooms(PageIndex page);

    PageData<RoomStudModel> listRooms(PageIndex page, int building) throws EntityNotFound;

    Optional<RoomModel> getRoom(int id);
}
