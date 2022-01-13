package nl.tudelft.sem11b.admin.data.filters;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.ApiDateTimeUtils;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.services.ReservationService;

public class AvailabilityFilter extends BaseFilter {
    private static final String NAME_FROM = "from";
    private static final String NAME_UNTIL = "until";
    private transient ApiDateTime from;
    private transient ApiDateTime until;
    private transient ReservationService reservations;

    /**
     * Constructor for availability filter.
     *
     * @param from         The time the room should be available from
     * @param until        The time the rooms should be available until
     * @param reservations The reservations client
     * @throws InvalidFilterException If from is after until
     */
    public AvailabilityFilter(ApiDateTime from, ApiDateTime until,
                              ReservationService reservations) throws InvalidFilterException {
        this.from = from;
        this.until = until;
        this.reservations = reservations;
        if (from.compareTo(until) > 0) {
            throw new InvalidFilterException("Timeslot must start before it ends!");
        }
    }

    @Override
    public boolean handle(Room room) {
        ReservationRequestModel requestModel = new ReservationRequestModel(room.getId(),
            "Check-availability", from, until, null);
        try {
            if (!reservations.checkAvailability(room.getId(), requestModel)) {
                return false;
            }
        } catch (InvalidData e) {
            e.printStackTrace();
            return false;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return super.handle(room);
    }

    /**
     * Constructs a new {@link AvailabilityFilter} from given filters.
     *
     * @param opt          Filter options
     * @param reservations Reservations service
     * @return A new filter; {@code Optional.none()} if filter was not given
     * @throws InvalidFilterException When given filter options are invalid
     */
    public static Optional<AvailabilityFilter> fromOptions(Map<String, Object> opt,
                                                           ReservationService reservations)
        throws InvalidFilterException {
        if (!opt.containsKey(NAME_FROM) && !opt.containsKey(NAME_UNTIL)) {
            return Optional.empty();
        }

        // If either filter is not there, the user probably made an error,
        // and we should let them know.
        if (!opt.containsKey(NAME_FROM) || !opt.containsKey(NAME_UNTIL)) {
            throw new InvalidFilterException("Either from or until time not provided!");
        }

        try {
            return Optional.of(new AvailabilityFilter(
                ApiDateTimeUtils.parse((String) opt.get(NAME_FROM)),
                ApiDateTimeUtils.parse((String) opt.get(NAME_UNTIL)), reservations));
        } catch (ClassCastException | ParseException e) {
            throw new InvalidFilterException("Invalid availability filter!");
        }
    }
}
