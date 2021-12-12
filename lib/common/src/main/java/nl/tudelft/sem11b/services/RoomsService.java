package nl.tudelft.sem11b.services;

import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.*;

public interface RoomsService {
    PageData<RoomStudModel> listRooms(PageIndex page);

    PageData<RoomStudModel> listRooms(PageIndex page, int building) throws EntityNotFound;

    List<RoomModel> searchRooms(PageIndex page, Integer capacity, EquipmentObject[] equipment,
                                String availableSince, String availableUntil, Integer building) throws EntityNotFound;

    Optional<RoomModel> getRoom(int id);
}
