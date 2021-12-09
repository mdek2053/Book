package nl.tudelft.sem11b.services;

import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;

public interface ReservationService {
    long makeOwnReservation(long roomId, String title, ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData;
}
