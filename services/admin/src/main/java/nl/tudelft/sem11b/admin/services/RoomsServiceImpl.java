package nl.tudelft.sem11b.admin.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Equipment;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.filters.AvailabilityFilter;
import nl.tudelft.sem11b.admin.data.filters.BaseFilter;
import nl.tudelft.sem11b.admin.data.filters.BuildingFilter;
import nl.tudelft.sem11b.admin.data.filters.CapacityFilter;
import nl.tudelft.sem11b.admin.data.filters.EquipmentFilter;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.admin.data.repositories.EquipmentRepository;
import nl.tudelft.sem11b.admin.data.repositories.RoomRepository;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.EquipmentModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.ReservationService;
import nl.tudelft.sem11b.services.RoomsService;
import nl.tudelft.sem11b.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RoomsServiceImpl implements RoomsService {
    private final transient String missingEntityName = "Room";

    private final transient BuildingRepository buildings;
    private final transient RoomRepository rooms;
    private final transient EquipmentRepository equipmentRepo;
    private final transient UserService users;
    private final transient ReservationService reservations;

    private final transient String serviceName = "Rooms";

    /**
     * Instantiates the {@link RoomsServiceImpl} class.
     *
     * @param buildings Building repository
     * @param rooms     Room repository
     * @param users     Users handling service
     */
    public RoomsServiceImpl(BuildingRepository buildings, RoomRepository rooms,
                            EquipmentRepository equipmentRepo, UserService users,
                            ReservationService reservations) {
        this.buildings = buildings;
        this.rooms = rooms;
        this.equipmentRepo = equipmentRepo;
        this.users = users;
        this.reservations = reservations;
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
        throws EntityNotFound, InvalidFilterException {

        BaseFilter chain = setupChain(filterValues);

        Page<Room> roomPage = rooms.findAll(page.getPage(Sort.by("id")));
        List<RoomStudModel> filteredRooms = new ArrayList<>();
        for (Room room : roomPage) {
            if (chain.handle(room)) {
                filteredRooms.add(room.toStudModel());
            }
        }

        return new PageData<>(filteredRooms.size(), filteredRooms);
    }

    private BaseFilter setupChain(Map<String, Object> filterValues)
        throws InvalidFilterException, EntityNotFound {
        var filter = new BaseFilter();

        filter
            .setNext(CapacityFilter.fromOptions(filterValues))
            .setNext(EquipmentFilter.fromOptions(filterValues, equipmentRepo))
            .setNext(BuildingFilter.fromOptions(filterValues, buildings))
            .setNext(AvailabilityFilter.fromOptions(filterValues, reservations));

        return filter;
    }

    @Override
    public Optional<RoomModel> getRoom(long id) {
        return rooms.findById(id).map(Room::toModel);
    }

    @Override
    public RoomModel addRoom(RoomModel model) throws ApiException, EntityNotFound {
        UserModel user = users.currentUser();
        verifyAdmin(user);
        BuildingModel buildingModel = model.getBuilding();
        if (buildingModel == null) {
            throw new EntityNotFound("Building");
        }
        Optional<Building> buildingOptional = buildings.findById(buildingModel.getId());

        if (buildingOptional.isEmpty()) {
            throw new EntityNotFound("Building");
        }

        Room newRoom = new Room(model.getSuffix(),
            model.getName(), model.getCapacity(), null, buildingOptional.get(), Set.of());

        Room saved = rooms.save(newRoom);

        //Only convert the closure to a model if it is not null
        ClosureModel savedClosure =
            saved.getClosure() == null ? null : saved.getClosure().toModel();

        return new RoomModel(saved.getId(), saved.getSuffix(),
            saved.getName(), saved.getCapacity(),
            saved.getBuilding().toModel(),
            saved.getEquipment().toArray(EquipmentModel[]::new), savedClosure);
    }

    @Override
    public EquipmentModel addEquipment(EquipmentModel model, Optional<Long> roomId)
        throws ApiException, EntityNotFound {
        var user = users.currentUser();
        verifyAdmin(user);
        if (roomId.isEmpty()) {
            return addEquipmentToSystem(model, false, null);
        }
        Long id = roomId.get();
        if (!rooms.existsById(id)) {
            throw new EntityNotFound("Room id does not exist");
        }
        EquipmentModel equipment;
        equipment = addEquipmentToSystem(model, true, id);

        return equipment;
    }

    private void verifyAdmin(UserModel user) throws ApiException {
        if (!user.inRole(Roles.Admin)) {
            throw new ApiException(serviceName,
                    "User not authorized to perform this action.");
        }
    }

    private EquipmentModel addEquipmentToSystem(EquipmentModel model, boolean roomPresent,
                                           Long roomId) {
        Equipment equipment;
        Optional<Equipment> optEquipment = equipmentRepo.findByName(model.getName());
        if (optEquipment.isPresent()) {
            equipment = optEquipment.get();
            if (roomPresent) {
                addEquipmentToRoom(equipment, roomId);
            }
            return equipment.toModel();
        }
        equipment = new Equipment(model.getName());
        if (roomPresent) {
            addEquipmentToRoom(equipment, roomId);
        }
        return equipmentRepo.save(equipment).toModel();
    }

    private void addEquipmentToRoom(Equipment equipment, Long roomId) {
        Room room = rooms.getById(roomId);
        room.addEquipment(equipment);
        rooms.save(room);
    }

    @Override
    public void closeRoom(long id, ClosureModel closure) throws EntityNotFound, ApiException {
        var user = users.currentUser();
        if (!user.inRole(Roles.Admin)) {
            throw new ApiException(serviceName,
                "User not authorized to close rooms.");
        }

        if (!rooms.existsById(id)) {
            throw new EntityNotFound(missingEntityName);
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
            throw new ApiException(serviceName,
                "User not authorized to open rooms.");
        }

        if (!rooms.existsById(id)) {
            throw new EntityNotFound(missingEntityName);
        }

        var room = rooms.getById(id);
        room.setClosure(null);
        rooms.save(room);
    }
}
