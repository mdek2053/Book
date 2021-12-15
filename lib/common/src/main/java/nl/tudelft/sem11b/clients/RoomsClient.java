package nl.tudelft.sem11b.clients;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.services.ReservationService;
import nl.tudelft.sem11b.services.RoomsService;

/**
 * A client for the {@link RoomsService} API. This client requires authentication.
 */
public class RoomsClient implements RoomsService {
    private final ApiClient<Authenticated> api;

    /**
     * Instantiates the {@link RoomsClient} class.
     *
     * @param api API client with authentication
     */
    public RoomsClient(ApiClient<Authenticated> api) {
        this.api = api;
    }

    @Override
    public PageData<RoomStudModel> listRooms(PageIndex page) throws ApiException {
        var uri = "/rooms?page=" + page.getPage() + "&limit=" + page.getLimit();
        return api.get(uri, new TypeReference<PageData<RoomStudModel>>() {
        }).unwrap();
    }

    @Override
    public PageData<RoomStudModel> listRooms(PageIndex page, long building)
        throws ApiException, EntityNotFound {
        var uri = "/buildings/" + building + "/rooms?page=" + page.getPage() + "&limit="
            + page.getLimit();
        var data = api.get(uri, new TypeReference<PageData<RoomStudModel>>() {
        }).toOptional();

        if (data.isEmpty()) {
            throw new EntityNotFound("Building");
        }

        return data.get();
    }

    @Override
    public PageData<RoomStudModel> searchRooms(PageIndex page, Map<String, Object> filterValues) throws ApiException, EntityNotFound, InvalidFilterException {
        return null;
    }

    @Override
    public Optional<RoomModel> getRoom(long id) throws ApiException {
        return api.get("/rooms/" + id, new TypeReference<RoomModel>() {}).toOptional();
    }
}
