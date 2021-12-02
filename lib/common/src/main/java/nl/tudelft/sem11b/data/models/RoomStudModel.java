package nl.tudelft.sem11b.data.models;

public class RoomStudModel {
    private final int id;
    private final String suffix;
    private final String name;
    private final int capacity;
    private final ClosureModel closure;

    public RoomStudModel(int id, String suffix, String name, int capacity, ClosureModel closure) {
        this.id = id;
        this.suffix = suffix;
        this.name = name;
        this.capacity = capacity;
        this.closure = closure;
    }

    public RoomStudModel(int id, String suffix, String name, int capacity) {
        this(id, suffix, name, capacity, null);
    }

    public int getId() {
        return id;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public ClosureModel getClosure() {
        return closure;
    }
}
