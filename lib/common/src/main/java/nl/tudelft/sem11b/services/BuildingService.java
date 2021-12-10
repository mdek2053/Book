package nl.tudelft.sem11b.services;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;

public interface BuildingService {
    PageData<BuildingModel> listBuildings(PageIndex page) throws ApiException;

    Optional<BuildingModel> getBuilding(long id) throws ApiException;
}
