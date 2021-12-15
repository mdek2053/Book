package nl.tudelft.sem11b.admin.data.filters;

import nl.tudelft.sem11b.admin.data.entities.Room;

public class AvailabilityFilter extends BaseFilter{
    private String from;
    private String until;

    public AvailabilityFilter(String from, String until) {
        this.from = from;
        this.until = until;
    }

    @Override
    public boolean handle(Room room) {
        return super.handle(room);
    }
}
