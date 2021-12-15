package nl.tudelft.sem11b.admin.data.filters;

import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;

public class BuildingFilter extends BaseFilter {

    private Building building;

    public BuildingFilter(long buildingId, BuildingRepository buildings) throws EntityNotFound {
        if (!buildings.existsById(buildingId)) {
            throw new EntityNotFound("Building");
        }
        this.building = buildings.getById(buildingId);
    }

    @Override
    public boolean handle(Room room) {
        if(!room.getBuilding().equals(building)) {
            return false;
        }

        return super.handle(room);
    }

}
