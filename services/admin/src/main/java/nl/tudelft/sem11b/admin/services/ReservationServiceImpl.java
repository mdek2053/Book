package nl.tudelft.sem11b.admin.services;

import java.net.URI;

import nl.tudelft.sem11b.clients.AuthenticatedServiceClient;
import nl.tudelft.sem11b.clients.ReservationClient;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.services.ReservationService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl extends AuthenticatedServiceClient<ReservationClient>
        implements ReservationService {

    public ReservationServiceImpl() {
        super(URI.create("http://localhost:8080/"), "Reservation", ReservationClient::new);
    }

    @Override
    public long makeOwnReservation(long roomId, String title, ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData {
        return 0;
    }

    @Override
    public PageData<ReservationModel> inspectOwnReservation(PageIndex page) throws ApiException {
        return null;
    }

    @Override
    public void editReservation(long reservationId, String title,
                                ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound, InvalidData {

    }

    @Override
    public void deleteReservation(long reservationId) throws ApiException, EntityNotFound {

    }

    @Override
    public boolean checkAvailability(long roomModelId, ReservationRequestModel requestModel)
            throws InvalidData, ApiException {
        return openClient().checkAvailability(roomModelId, requestModel);
    }
}
