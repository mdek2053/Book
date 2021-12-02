package nl.tudelft.sem11b.services;

import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.ForbiddenException;
import nl.tudelft.sem11b.data.exception.NotFoundException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;

public interface ReservationService {
    long makeOwnReservation(long room_id, String user_token, String title, String since, String until)
            throws ForbiddenException, CommunicationException, NotFoundException, UnauthorizedException;
}
