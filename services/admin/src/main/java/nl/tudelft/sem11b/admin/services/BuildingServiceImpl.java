package nl.tudelft.sem11b.admin.services;

import java.util.Optional;
import java.util.Set;

import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.BuildingService;
import nl.tudelft.sem11b.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildings;
    private final UserService users;

    public BuildingServiceImpl(BuildingRepository buildings, UserService users) {
        this.buildings = buildings;
        this.users = users;
    }

    @Override
    public PageData<BuildingModel> listBuildings(PageIndex page) {
        return new PageData<>(
            buildings.findAll(page.getPage(Sort.by("id"))).map(Building::toModel));
    }

    @Override
    public Optional<BuildingModel> getBuilding(long id) {
        return buildings.findById(id).map(Building::toModel);
    }

    @Override
    public void addBuilding(BuildingModel model) throws ApiException {
        UserModel user = users.currentUser();
        if (!user.inRole(Roles.Admin)) {
            throw new ApiException("Rooms",
                    "User not authorized to add buildings");
        }
        if (buildings.existsById(model.getId())) {
            throw new ApiException("Rooms",
                    "Building id already exists");
        }

        Building building = new Building(model.getId(), model.getPrefix(), model.getName(), model.getOpen(), model.getClose(), Set.of());
        buildings.save(building);
    }
}
