package nl.tudelft.sem11b.admin.data.filters;

import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.services.ReservationService;

public class AvailabilityFilter extends BaseFilter {
    private transient ApiDateTime from;
    private transient ApiDateTime until;
    private transient ReservationService reservations;

    public AvailabilityFilter(ApiDateTime from, ApiDateTime until, ReservationService reservations) throws InvalidFilterException {
        this.from = from;
        this.until = until;
        this.reservations = reservations;
        if(from.compareTo(until) > 0) {
            throw new InvalidFilterException("Timeslot must start before it ends!");
        }
    }

    @Override
    public boolean handle(Room room) {
        ReservationRequestModel requestModel =
                new ReservationRequestModel(room.getId(), "Check-availability", from, until, 1L);
        try {
            if(!reservations.checkAvailability(room.getId(), requestModel)) {
                return false;
            }
        } catch (InvalidData e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return super.handle(room);
    }
}
