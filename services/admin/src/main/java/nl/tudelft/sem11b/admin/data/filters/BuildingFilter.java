package nl.tudelft.sem11b.admin.data.filters;

import java.util.Map;
import java.util.Optional;

import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;

public class BuildingFilter extends BaseFilter {
    public static final String NAME = "building";
    private final transient Building building;

    /**
     * Constructor for BuildingFilter, checks whether building actually exists.
     *
     * @param building The building
     */
    public BuildingFilter(Building building) {
        this.building = building;
    }

    @Override
    public boolean handle(Room room) {
        if (!room.getBuilding().equals(building)) {
            return false;
        }

        return super.handle(room);
    }

    /**
     * Constructs a new {@link BuildingFilter} from given filters.
     *
     * @param opt       Filter options
     * @param buildings Buildings repository
     * @return A new filter; {@code Optional.none()} if filter was not given
     * @throws InvalidFilterException When given filter options are invalid
     */
    public static Optional<BuildingFilter> fromOptions(Map<String, Object> opt,
                                                       BuildingRepository buildings)
        throws InvalidFilterException, EntityNotFound {
        if (!opt.containsKey(NAME)) {
            return Optional.empty();
        }

        long id;
        try {
            id = (Long) opt.get(NAME);
        } catch (ClassCastException e) {
            throw new InvalidFilterException("Invalid building filter!");
        }

        var building = buildings.findById(id);
        if (building.isEmpty()) {
            throw new EntityNotFound("Building");
        }

        return Optional.of(new BuildingFilter(building.get()));
    }
}
