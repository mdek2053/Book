package nl.tudelft.sem11b.clients;

import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.services.FaultService;

public class FaultsClient implements FaultService {
    private final transient ApiClient<Authenticated> api;

    /**
     * Instantiates the {@link FaultsClient} class.
     *
     * @param api API client with authentication
     */
    public FaultsClient(ApiClient<Authenticated> api) {
        this.api = api;
    }

    @Override
    public void addFault(long roomId, FaultRequestModel faultRequest) throws ApiException {
        var response = api.post("/rooms/" + roomId + "/faults",
            faultRequest, new TypeReference<>() {});

        if (!response.isSuccessful()) {
            throw response.getError();
        }
    }

    @Override
    public PageData<FaultStudModel> listFaults(PageIndex page, long roomId)
        throws ApiException, EntityNotFound {
        var uri = "/rooms/" + roomId + "/faults?page=" + page.getPage() + "&limit="
            + page.getLimit();
        var data = api.get(uri,
            new TypeReference<PageData<FaultStudModel>>() {}).toOptional();

        if (data.isEmpty()) {
            throw new EntityNotFound("Room");
        }

        return data.get();
    }

    @Override
    public PageData<FaultModel> listFaults(PageIndex page) throws ApiException, EntityNotFound {

        var uri = "/faults?page=" + page.getPage() + "&limit=" + page.getLimit();
        var data = api.get(uri,
            new TypeReference<PageData<FaultModel>>() {}).toOptional();

        if (data.isEmpty()) {
            throw new EntityNotFound("Fault");
        }

        return data.get();
    }

    @Override
    public Optional<FaultModel> getFault(long id) throws ApiException {
        return api.get("/faults/" + id, new TypeReference<FaultModel>() {}).toOptional();
    }

    @Override
    public void resolveFault(long id) throws ApiException {
        var response = api.delete("/faults/" + id);

        if (!response.isSuccessful()) {
            throw response.getError();
        }
    }
}
