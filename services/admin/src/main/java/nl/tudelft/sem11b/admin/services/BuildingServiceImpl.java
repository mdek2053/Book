package nl.tudelft.sem11b.admin.services;

import java.util.Optional;

import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.services.BuildingService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildings;

    public BuildingServiceImpl(BuildingRepository buildings) {
        this.buildings = buildings;
    }

    @Override
    public PageData<BuildingModel> listBuildings(PageIndex page) {
        return new PageData<>(
            buildings.findAll(page.getPage(Sort.by("id"))).map(Building::toModel));
    }

    @Override
    public Optional<BuildingModel> getBuilding(int id) {
        return buildings.findById(id).map(Building::toModel);
    }
}
