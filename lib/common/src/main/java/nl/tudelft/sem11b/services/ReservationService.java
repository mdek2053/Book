package nl.tudelft.sem11b.services;

import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.ForbiddenException;
import nl.tudelft.sem11b.data.exception.NotFoundException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;
import nl.tudelft.sem11b.data.models.ReservationModel;

import java.util.List;

public interface ReservationService {
    long makeOwnReservation(long roomId, String userToken,
                            String title, String since, String until)
            throws ForbiddenException, CommunicationException,
            NotFoundException, UnauthorizedException;

    List<ReservationModel> inspectOwnReservation(String token) throws CommunicationException, UnauthorizedException, NoSuchFieldException, IllegalAccessException;
}
