package nl.tudelft.sem11b.admin.data.filters;

import nl.tudelft.sem11b.admin.data.entities.Room;

public class BaseFilter {

    protected BaseFilter next;

    public void setNext(BaseFilter next) {
        this.next = next;
    }

    /**
     * Forwards the room to the next filter in the chain, or returns true if end of chain.
     *
     * @param room the room to be filtered
     * @return the result of the filter method
     */
    public boolean handle(Room room) {
        if (next == null) {
            return true;
        }
        return next.handle(room);
    }

}
