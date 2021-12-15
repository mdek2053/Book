package nl.tudelft.sem11b.admin.data.filters;

import nl.tudelft.sem11b.admin.data.entities.Room;

public class CapacityFilter extends BaseFilter{

    private int minCapacity;

    public CapacityFilter(int minCapacity) {
        this.minCapacity = minCapacity;
    }

    @Override
    public boolean handle(Room room){
        if(room.getCapacity() < this.minCapacity){
            return false;
        }
        return super.handle(room);
    }
}
