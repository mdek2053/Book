package nl.tudelft.sem11b.data.models;

public class RoomModel {
    private final int id;
    private final String suffix;
    private final String name;
    private final int capacity;
    private final BuildingModel building;
    private final ClosureModel closure;

    public RoomModel(int id, String suffix, String name, int capacity, BuildingModel building, ClosureModel closure) {
        this.id = id;
        this.suffix = suffix;
        this.name = name;
        this.capacity = capacity;
        this.building = building;
        this.closure = closure;
    }

    public RoomModel(int id, String suffix, String name, int capacity, BuildingModel building) {
        this(id, suffix, name,capacity, building, null);
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

    public BuildingModel getBuilding() {
        return building;
    }

    public ClosureModel getClosure() {
        return closure;
    }
}
