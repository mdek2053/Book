package nl.tudelft.sem11b.admin.data.filters;

import java.util.Optional;

import nl.tudelft.sem11b.admin.data.entities.Room;

public class BaseFilter {

    protected transient BaseFilter next;

    public void setNext(BaseFilter next) {
        this.next = next;
    }

    /**
     * Sets the next filter in the chain, if provided.
     *
     * @param filter A filter to attach (if any)
     * @param <T>    Type of filter
     * @return Next filter in the chain. Necessarily equal to {@code this} if filter is not given
     */
    public <T extends BaseFilter> BaseFilter setNext(Optional<T> filter) {
        if (filter.isEmpty()) {
            return this;
        }

        this.next = filter.get();
        return this.next;
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
