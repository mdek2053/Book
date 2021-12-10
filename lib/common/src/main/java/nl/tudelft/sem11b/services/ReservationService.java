package nl.tudelft.sem11b.services;

import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;

public interface ReservationService {
    long makeOwnReservation(long roomId, String title, ApiDateTime since, ApiDateTime until)
        throws ApiException, EntityNotFound, InvalidData;

    PageData<ReservationModel> inspectOwnReservation(PageIndex page) throws ApiException;

    void editReservation(long reservationId, String title, ApiDateTime since, ApiDateTime until)
        throws ApiException, EntityNotFound, InvalidData;
}
