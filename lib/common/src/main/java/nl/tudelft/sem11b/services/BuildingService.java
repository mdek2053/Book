package nl.tudelft.sem11b.services;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;

/**
 * API definition of the building service. This service is responsible for holding the information
 * about buildings.
 */
public interface BuildingService {
    /**
     * Lists a page of all buildings in the system.
     *
     * @param page Page index to fetch
     * @return The requested page of buildings
     * @throws ApiException Thrown when a remote API encountered an error
     */
    PageData<BuildingModel> listBuildings(PageIndex page) throws ApiException;

    /**
     * Gets information about a building with the given unique numeric identifier.
     *
     * @param id The unique numeric identifier of the building to fetch
     * @return Building information if found; an empty optional otherwise
     * @throws ApiException Thrown when a remote API encountered an error
     */
    Optional<BuildingModel> getBuilding(long id) throws ApiException;

    void addBuilding(BuildingModel model) throws ApiException;
}
