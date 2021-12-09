package nl.tudelft.sem11b.clients;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.services.ReservationService;

public class ReservationClient implements ReservationService {
    private final ApiClient<Authenticated> api;

    public ReservationClient(ApiClient<Authenticated> api) {
        this.api = api;
    }

    @Override
    public long makeOwnReservation(long roomId, String title, ApiDateTime since, ApiDateTime until)
        throws ApiException, EntityNotFound, InvalidData {
        var req = new ReservationRequestModel(roomId, title, since, until, null);

        var res = api.post("/reservations", req,
                new TypeReference<>() {}, new TypeReference<IdModel<Long>>() {})
            .toOptional();

        if (res.isEmpty()) {
            throw new EntityNotFound("Room");
        }

        return res.get().getId();
    }
}
