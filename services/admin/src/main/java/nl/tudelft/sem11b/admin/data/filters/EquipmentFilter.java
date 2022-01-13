package nl.tudelft.sem11b.admin.data.filters;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import nl.tudelft.sem11b.admin.data.entities.Equipment;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.EquipmentRepository;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;

public class EquipmentFilter extends BaseFilter {
    public static final String NAME = "equipment";
    private transient Set<Equipment> equipment = new HashSet<>();
    private transient EquipmentRepository repo;

    /**
     * Constructor for EquipmentFilter. Fetches all required equipment from database.
     *
     */
    public EquipmentFilter(Set<Equipment> equipment) {
        this.equipment = equipment;
    }

    @Override
    public boolean handle(Room room) {
        if (!room.getEquipment().containsAll(equipment)) {
            return false;
        }
        return super.handle(room);
    }

    /**
     * Constructs a new {@link EquipmentFilter} from given filters.
     *
     * @param opt       Filter options
     * @param equipment Equipment repository
     * @return A new filter; {@code Optional.none()} if filter was not given
     * @throws InvalidFilterException When given filter options are invalid
     */
    public static Optional<EquipmentFilter> fromOptions(Map<String, Object> opt,
                                                        EquipmentRepository equipment)
        throws InvalidFilterException, EntityNotFound {
        if (!opt.containsKey("equipment")) {
            return Optional.empty();
        }

        Set<Long> ids;
        try {
            ids = (Set<Long>) opt.get("equipment");
        } catch (ClassCastException e) {
            throw new InvalidFilterException("Invalid equipment filter!");
        }

        var entities = equipment.findAllById(ids);
        if (entities.size() != ids.size()) {
            throw new EntityNotFound("Equipment");
        }

        return Optional.of(new EquipmentFilter(new HashSet<>(entities)));
    }
}
