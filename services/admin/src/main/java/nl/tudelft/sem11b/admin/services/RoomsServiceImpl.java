package nl.tudelft.sem11b.admin.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.filters.AvailabilityFilter;
import nl.tudelft.sem11b.admin.data.filters.BaseFilter;
import nl.tudelft.sem11b.admin.data.filters.BuildingFilter;
import nl.tudelft.sem11b.admin.data.filters.CapacityFilter;
import nl.tudelft.sem11b.admin.data.filters.EquipmentFilter;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.admin.data.repositories.RoomRepository;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.services.RoomsService;
import nl.tudelft.sem11b.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RoomsServiceImpl implements RoomsService {
    private final BuildingRepository buildings;
    private final RoomRepository rooms;
    private final UserService users;

    /**
     * Instantiates the {@link RoomsServiceImpl} class.
     *
     * @param buildings Building repository
     * @param rooms     Room repository
     * @param users     Users handling service
     */
    public RoomsServiceImpl(BuildingRepository buildings, RoomRepository rooms, UserService users) {
        this.buildings = buildings;
        this.rooms = rooms;
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
    public PageData<RoomStudModel> searchRooms(PageIndex page, Map<String, Object> filterValues)
            throws ApiException, EntityNotFound, InvalidFilterException {

        BaseFilter chain = setupChain(filterValues);

        Page<Room> roomPage = rooms.findAll(page.getPage(Sort.by("id")));
        List<RoomStudModel> filteredRooms = new ArrayList<RoomStudModel>();
        for (Room room : roomPage) {
            if (chain.handle(room)) {
                filteredRooms.add(room.toStudModel());
            }
        }

        return new PageData<RoomStudModel>(filteredRooms.size(), filteredRooms);
    }

    private BaseFilter setupChain(Map<String, Object> filterValues)
            throws InvalidFilterException, EntityNotFound {
        BaseFilter head = new BaseFilter();
        BaseFilter tail = head;

        if (filterValues.containsKey("capacity")) {
            try {
                BaseFilter filter = new CapacityFilter((Integer)filterValues.get("capacity"));
                tail.setNext(filter);
                tail = filter;
            } catch (ClassCastException e) {
                throw new InvalidFilterException("Invalid capacity filter!");
            }
        }

        if (filterValues.containsKey("equipment")) {
            try {
                BaseFilter filter = new EquipmentFilter();
                tail.setNext(filter);
                tail = filter;
            } catch (ClassCastException e) {
                throw new InvalidFilterException("Invalid equipment filter!");
            }
        }

        if (filterValues.containsKey("from") || filterValues.containsKey("until")) {
            try {
                BaseFilter filter = new AvailabilityFilter((String)filterValues.get("from"),
                        (String)filterValues.get("until"));
                tail.setNext(filter);
                tail = filter;
            } catch (ClassCastException e) {
                throw new InvalidFilterException("Invalid availability filter!");
            }
        }

        if (filterValues.containsKey("building")) {
            try {
                BaseFilter filter =
                        new BuildingFilter((Long)filterValues.get("building"), buildings);
                tail.setNext(filter);
                tail = filter;
            } catch (ClassCastException e) {
                throw new InvalidFilterException("Invalid building filter!");
            }
        }

        return head;
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

        Room room = rooms.getById(id);
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

        Room room = rooms.getById(id);
        room.setClosure(null);
        rooms.save(room);
    }
}
