package nl.tudelft.sem11b.clients;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.EquipmentModel;
import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.services.RoomsService;

/**
 * A client for the {@link RoomsService} API. This client requires authentication.
 */
public class RoomsClient implements RoomsService {
    private static final String urlRoomsPrefix = "/rooms";
    private static final String urlLimitArg = "&limit=";
    private final transient ApiClient<Authenticated> api;

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
        var uri = urlRoomsPrefix + "?page=" + page.getPage() + urlLimitArg + page.getLimit();
        return api.get(uri, new TypeReference<PageData<RoomStudModel>>() {
        }).unwrap();
    }

    @Override
    public PageData<RoomStudModel> listRooms(PageIndex page, long building)
        throws ApiException, EntityNotFound {
        var uri = "/buildings/" + building + urlRoomsPrefix + "?page="
                + page.getPage() + urlLimitArg + page.getLimit();
        var data = api.get(uri, new TypeReference<PageData<RoomStudModel>>() {
        }).toOptional();

        if (data.isEmpty()) {
            throw new EntityNotFound("Building");
        }

        return data.get();
    }

    @Override
    public PageData<RoomStudModel> searchRooms(PageIndex page, Map<String, Object> filterValues)
            throws ApiException, EntityNotFound, InvalidFilterException {
        return null;
    }

    @Override
    public Optional<RoomModel> getRoom(long id) throws ApiException {
        return api.get(urlRoomsPrefix + "/" + id,
                new TypeReference<RoomModel>() {}).toOptional();
    }

    @Override
    public RoomModel addRoom(RoomModel model) {
        return null;
    }

    @Override
    public EquipmentModel addEquipment(EquipmentModel model, Optional<Long> roomId) {
        return null;
    }

    @Override
    public void closeRoom(long id, ClosureModel closure) throws ApiException {
        var response = api.post(urlRoomsPrefix + "/" + id + "/closure",
                closure, new TypeReference<>() {});

        if (!response.isSuccessful()) {
            throw response.getError();
        }
    }

    @Override
    public void reopenRoom(long id) throws ApiException {
        var response = api.delete(urlRoomsPrefix + "/" + id + "/closure");

        if (!response.isSuccessful()) {
            throw response.getError();
        }
    }
}
