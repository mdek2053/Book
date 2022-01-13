package nl.tudelft.sem11b.admin.data.filters;

import java.util.Map;
import java.util.Optional;

import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;

public class CapacityFilter extends BaseFilter {
    public static final String NAME = "capacity";
    private final transient int minCapacity;

    public CapacityFilter(int minCapacity) {

        this.minCapacity = minCapacity;
    }

    @Override
    public boolean handle(Room room) {
        if (room.getCapacity() < this.minCapacity) {
            return false;
        }
        return super.handle(room);
    }

    /**
     * Constructs a new {@link CapacityFilter} from given filters.
     *
     * @param opt Filter options
     * @return A new filter; {@code Optional.none()} if filter was not given
     * @throws InvalidFilterException When given filter options are invalid
     */
    public static Optional<CapacityFilter> fromOptions(Map<String, Object> opt)
        throws InvalidFilterException {
        if (!opt.containsKey(NAME)) {
            return Optional.empty();
        }

        try {
            return Optional.of(new CapacityFilter((Integer) opt.get(NAME)));
        } catch (ClassCastException e) {
            throw new InvalidFilterException("Invalid capacity filter!");
        }
    }
}
