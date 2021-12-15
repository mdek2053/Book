package nl.tudelft.sem11b.admin.services;

import java.util.Optional;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.admin.data.entities.Fault;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.admin.data.repositories.FaultRepository;
import nl.tudelft.sem11b.admin.data.repositories.RoomRepository;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.services.RoomsService;
import nl.tudelft.sem11b.services.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RoomsServiceImpl implements RoomsService {
    private final BuildingRepository buildings;
    private final RoomRepository rooms;
    private final FaultRepository faults;
    private final UserService users;

    /**
     * Instantiates the {@link RoomsServiceImpl} class.
     *
     * @param buildings Building repository
     * @param rooms     Room repository
     * @param users     Users handling service
     */
    public RoomsServiceImpl(BuildingRepository buildings, RoomRepository rooms,
                            FaultRepository faults, UserService users) {
        this.buildings = buildings;
        this.rooms = rooms;
        this.faults = faults;
        this.users = users;
    }

    @Override
    public PageData<RoomStudModel> listRooms(PageIndex page) {
        return new PageData<>(rooms.findAll(page.getPage(Sort.by("id"))).map(Room::toStudModel));
    }

    @Override
    public PageData<RoomStudModel> listRooms(PageIndex page, long building) throws EntityNotFound {
        if (!buildings.existsById(building)) {
            throw new EntityNotFound("Building");
        }

        return new PageData<>(rooms.findAllByBuildingId(building, page.getPage(Sort.by("id")))
            .map(Room::toStudModel));
    }

    @Override
    public Optional<RoomModel> getRoom(long id) {
        return rooms.findById(id).map(Room::toModel);
    }

    @Override
    public void closeRoom(long id, ClosureModel closure) throws EntityNotFound, ApiException {
        var user = users.currentUser();
        if (!user.inRole(Roles.Admin)) {
            throw new ApiException("Rooms",
                    "User not authorized to close rooms.");
        }

        if (!rooms.existsById(id)) {
            throw new EntityNotFound("Room");
        }

        var room = rooms.getById(id);
        room.setClosure(new Closure(closure.getReason(),
                closure.getSince(),
                closure.getUntil()));
        rooms.save(room);
    }

    @Override
    public void reopenRoom(long id) throws EntityNotFound, ApiException {
        var user = users.currentUser();
        if (!user.inRole(Roles.Admin)) {
            throw new ApiException("Rooms",
                    "User not authorized to open rooms.");
        }

        if (!rooms.existsById(id)) {
            throw new EntityNotFound("Room");
        }

        var room = rooms.getById(id);
        room.setClosure(null);
        rooms.save(room);
    }

    @Override
    public void addFault(long roomId, FaultRequestModel faultRequest)
            throws ApiException, EntityNotFound {
        if (!rooms.existsById(roomId)) {
            throw new EntityNotFound("Room");
        }

        var room = rooms.getById(roomId);

        var user = users.currentUser();
        var fault = new Fault(user.getId(),
                faultRequest.getDescription(),
                room);
        faults.save(fault);
    }

    @Override
    public PageData<FaultStudModel> listFaults(PageIndex page, long roomId)
            throws EntityNotFound {

        if (!rooms.existsById(roomId)) {
            throw new EntityNotFound("Room");
        }

        return new PageData<>(faults.findAllByRoomId(roomId, page.getPage(Sort.by("id")))
                .map(Fault::toStudModel));
    }

    @Override
    public PageData<FaultModel> listFaults(PageIndex page) {
        return new PageData<>(faults.findAll(page.getPage(Sort.by("id")))
                .map(Fault::toModel));
    }

    @Override
    public Optional<FaultModel> getFault(long id) {
        return faults.findById(id).map(Fault::toModel);
    }

    @Override
    public void resolveFault(long id) throws EntityNotFound, ApiException {
        var user = users.currentUser();
        if (!user.inRole(Roles.Admin)) {
            throw new ApiException("Faults",
                    "User not authorized to resolve faults.");
        }

        if (!faults.existsById(id)) {
            throw new EntityNotFound("Fault");
        }

        var fault = faults.getById(id);
        faults.delete(fault);
    }
}
