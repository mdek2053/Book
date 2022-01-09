package nl.tudelft.sem11b.clients;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.IdModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.services.ReservationService;

/**
 * A client for the {@link ReservationService} API. This client requires authentication.
 */
public class ReservationClient implements ReservationService {
    private final transient ApiClient<Authenticated> api;

    /**
     * Instantiates the {@link ReservationClient} class.
     *
     * @param api API client with authentication
     */
    public ReservationClient(ApiClient<Authenticated> api) {
        this.api = api;
    }

    @Override
    public long makeOwnReservation(long roomId, String title, ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound {
        var req = new ReservationRequestModel(roomId, title, since, until, null);

        var res = api.post("/reservations", req,
                new TypeReference<>() {
                }, new TypeReference<IdModel<Long>>() {
                })
                .toOptional();

        if (res.isEmpty()) {
            throw new EntityNotFound("Room");
        }

        return res.get().getId();
    }

    @Override
    public long makeUserReservation(long roomId, Long forUser, String title,
                                    ApiDateTime since, ApiDateTime until)
            throws ApiException, EntityNotFound {
        var req = new ReservationRequestModel(roomId, title, since, until, forUser);

        var res = api.post("/reservations", req,
                new TypeReference<>() {
                }, new TypeReference<IdModel<Long>>() {
                })
                .toOptional();

        if (res.isEmpty()) {
            throw new EntityNotFound("Room");
        }

        return res.get().getId();
    }

    @Override
    public PageData<ReservationModel> inspectOwnReservation(PageIndex page) throws ApiException {
        var path = "/reservations/mine?page=" + page.getPage() + "&limit=" + page.getLimit();

        return api.get(path, new TypeReference<PageData<ReservationModel>>() {
        }).unwrap();
    }

    @Override
    public void editReservation(long reservationId, String title, ApiDateTime since,
                                ApiDateTime until) throws ApiException {
        var model = new ReservationModel(reservationId, since, until, title);
        api.post("/reservations/" + reservationId, model, new TypeReference<>() {
        }).unwrap();
    }

    @Override
    public void deleteReservation(long reservationId) throws ApiException {
        api.delete("/reservations/" + reservationId).unwrap();
    }

    @Override
    public boolean checkAvailability(long roomModelId, ReservationRequestModel requestModel)
            throws ApiException {
        return api.post("/reservations/availability/" + roomModelId, requestModel,
                new TypeReference<ReservationRequestModel>() {}, new TypeReference<Boolean>() {})
                .unwrap();
    }
}
