package nl.tudelft.sem11b.admin.data.filters;

import nl.tudelft.sem11b.admin.data.entities.Room;

public class BuildingFilter extends BaseFilter {

    private int buildingId;

    public BuildingFilter(int buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public boolean handle(Room room) {
        return super.handle(room);
    }

}
