package nl.tudelft.sem11b.admin.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.admin.data.repositories.RoomRepository;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.*;
import nl.tudelft.sem11b.services.RoomsService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RoomsServiceImpl implements RoomsService {
    private final BuildingRepository buildings;
    private final RoomRepository rooms;

    public RoomsServiceImpl(BuildingRepository buildings, RoomRepository rooms) {
        this.buildings = buildings;
        this.rooms = rooms;
    }

    @Override
    public PageData<RoomStudModel> listRooms(PageIndex page) {
        return new PageData<>(rooms.findAll(page.getPage(Sort.by("id"))).map(Room::toStudModel));
    }

    @Override
    public PageData<RoomStudModel> listRooms(PageIndex page, int building) throws EntityNotFound {
        if (!buildings.existsById(building)) {
            throw new EntityNotFound("Building");
        }

        return new PageData<>(rooms.findAllByBuildingId(building, page.getPage(Sort.by("id")))
            .map(Room::toStudModel));
    }

    @Override
    public List<RoomModel> searchRooms(PageIndex page, Integer capacity, EquipmentObject[] equipment,
                                       String availableSince, String availableUntil, Integer building) throws EntityNotFound{
        if (building != null && !buildings.existsById(building)) {
            throw new EntityNotFound("Building");
        }

        List<Room> roomList = rooms.findAll();
        List<RoomModel> roomModels = roomList.stream()
                .filter(room -> filterCapacity(room, capacity))
                .filter(room -> filterEquipment(room, equipment))
                .filter(room -> filterAvailability(room, availableSince, availableUntil))
                .filter(room -> filterBuilding(room, building))
                .map(Room::toModel)
                .collect(Collectors.toList());
        return roomModels;
    }

    private boolean filterCapacity(Room r, Integer capacity) {
        return capacity == null || r.getCapacity() <= capacity;
    }

    //TODO: implement this
    private boolean filterEquipment(Room r, EquipmentObject[] equipment){
        return true;
    }

    //TODO: implement this
    private boolean filterAvailability(Room r, String availableSince, String availableUntil) {
        return true;
    }

    private boolean filterBuilding(Room r, Integer building){
        if(building == null) return true;
        return buildings.findById(building).get().equals(r.getBuilding());
    }

    @Override
    public Optional<RoomModel> getRoom(int id) {
        return rooms.findById(id).map(Room::toModel);
    }
}
