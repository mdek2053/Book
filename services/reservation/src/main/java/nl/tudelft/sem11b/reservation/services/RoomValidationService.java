package nl.tudelft.sem11b.reservation.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.services.RoomsService;
import org.springframework.stereotype.Service;

@Service
public class RoomValidationService {

    private final transient ReservationRepository reservations;
    private final transient RoomsService rooms;

    public RoomValidationService(ReservationRepository reservations, RoomsService rooms) {
        this.reservations = reservations;
        this.rooms = rooms;
    }

    /**
     * Validates time of reservation.
     * @throws InvalidData When data is invalid.
     */
    public void validateTime(ReservationRequestModel requestModel) throws InvalidData {
        var now = LocalDateTime.now();
        var sinceJava = requestModel.getSince().toLocal();
        var untilJava = requestModel.getUntil().toLocal();

        // check if since comes before until
        if (sinceJava.compareTo(untilJava) >= 0) {
            throw new InvalidData("Reservation ends before it starts");
        }

        // check if the reservation is not in the past
        if (sinceJava.compareTo(now) < 0) {
            throw new InvalidData("Reservation is in the past");
        }

        // check if the reservation is not too far in the future
        int maxDaysInFuture = 14;
        if (now.until(sinceJava, ChronoUnit.DAYS) >= maxDaysInFuture) {
            throw new InvalidData("Reservation is more than two weeks away");
        }

        // check if the reservation spans multiple days
        if (!requestModel.getSince().getDate().equals(requestModel.getUntil().getDate())) {
            throw new InvalidData("Reservation spans multiple days");
        }
    }

    private void validateBusinessHours(ApiTime reservationSince, ApiTime reservationUntil,
                                       ApiTime buildingOpen, ApiTime buildingClose)
            throws InvalidData {
        if (buildingOpen.compareTo(reservationSince) > 0
                || buildingClose.compareTo(reservationUntil) < 0) {
            throw new InvalidData("Reservation not between room opening hours");
        }
    }

    /**
     * Validates the room of reservation.
     * @param roomId the Id of the room.
     * @throws InvalidData When data is invalid.
     * @throws EntityNotFound When entity is not found.
     * @throws ApiException When communication with different microservice fails.
     */
    public void validateRoom(Long roomId, ReservationRequestModel requestModel)
            throws InvalidData, EntityNotFound, ApiException {
        ApiDateTime since = requestModel.getSince();
        ApiDateTime until = requestModel.getUntil();

        var room = getRoomIfExists(roomId);
        var building = room.getBuilding();
        // check if in business hours
        validateBusinessHours(since.getTime(), until.getTime(),
                building.getOpen(), building.getClose());

        // room should also NOT be under maintenance
        var closure = room.getClosure();
        if (closure != null) {
            closure.verify(since);
        }


        // closure has already ended
    }

    private RoomModel getRoomIfExists(Long roomId)
            throws InvalidData, EntityNotFound, ApiException {
        if (roomId == null) {
            throw new InvalidData("No room specified");
        }
        var roomOpt = rooms.getRoom(roomId);
        if (roomOpt.isEmpty()) {
            throw new EntityNotFound("Room");
        }
        return roomOpt.get();
    }

    /**
     * Checks if the time of reservation doesn't conflict with any other reservation.
     * @param roomId Id of room.
     * @throws InvalidData When data is invalid.
     */
    public void validateRoomConflicts(Long roomId, ReservationRequestModel requestModel)
            throws InvalidData {
        var sinceTs = requestModel.getSince().toTimestamp();
        var untilTs = requestModel.getUntil().toTimestamp();
        if (reservations.hasRoomConflict(roomId, sinceTs, untilTs)) {
            throw new InvalidData("Reservation conflicts with room's existing reservations");
        }
    }

}
