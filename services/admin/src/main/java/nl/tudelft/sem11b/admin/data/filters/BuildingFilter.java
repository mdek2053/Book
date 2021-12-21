package nl.tudelft.sem11b.admin.data.filters;

import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;

public class BuildingFilter extends BaseFilter {

    private transient Building building;

    /**
     * Constructor for BuildingFilter, checks whether building actually exists.
     * @param buildingId    The id of the building
     * @param buildings     The building repository
     * @throws EntityNotFound When the building is not in the repository
     */
    public BuildingFilter(long buildingId, BuildingRepository buildings) throws EntityNotFound {
        if (!buildings.existsById(buildingId)) {
            throw new EntityNotFound("Building");
        }
        this.building = buildings.getById(buildingId);
    }

    @Override
    public boolean handle(Room room) {
        if (!room.getBuilding().equals(building)) {
            return false;
        }

        return super.handle(room);
    }

}
