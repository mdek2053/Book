package nl.tudelft.sem11b.clients;

import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.services.BuildingService;

/**
 * A client for the {@link BuildingService} API. This client requires authentication.
 */
public class BuildingClient implements BuildingService {
    private final ApiClient<Authenticated> api;

    /**
     * Instantiates the {@link BuildingClient} class.
     *
     * @param api API client with authentication
     */
    public BuildingClient(ApiClient<Authenticated> api) {
        this.api = api;
    }

    @Override
    public PageData<BuildingModel> listBuildings(PageIndex page) throws ApiException {
        var path = "/buildings?page=" + page.getPage() + "&limit=" + page.getLimit();
        return api.get(path, new TypeReference<PageData<BuildingModel>>() {}).unwrap();
    }

    @Override
    public Optional<BuildingModel> getBuilding(long id) throws ApiException {
        return api.get("/buildings/" + id, new TypeReference<BuildingModel>() {}).toOptional();
    }

    @Override
    public BuildingModel addBuilding(BuildingModel model) {
        return null;
    }
}
