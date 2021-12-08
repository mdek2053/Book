package nl.tudelft.sem11b.services;

import java.util.Optional;

import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;

public interface BuildingService {
    PageData<BuildingModel> listBuildings(PageIndex page);

    Optional<BuildingModel> getBuilding(int id);
}