package nl.tudelft.sem11b.admin.data.filters;

import nl.tudelft.sem11b.admin.data.entities.Room;

public class BaseFilter {

    protected BaseFilter next;

    public void setNext(BaseFilter next) {
        this.next = next;
    }

    public boolean handle(Room room){
        if(next == null) {
            return true;
        }
        return next.handle(room);
    }

}
