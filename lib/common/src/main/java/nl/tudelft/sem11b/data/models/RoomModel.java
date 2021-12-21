package nl.tudelft.sem11b.data.models;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Holds all room information.
 */
public class RoomModel {
    private Long id;
    private String suffix;
    private String name;
    private int capacity;
    private BuildingModel building;
    private EquipmentModel[] equipment;
    private ClosureModel closure;

    /**
     * Instantiates the {@link RoomModel} class.
     *
     * @param id       Unique numeric identifier of the room
     * @param suffix   Room suffix
     * @param name     Room name
     * @param capacity Room maximal capacity
     * @param building The building information object
     * @param closure  The room closure (if any)
     */
    public RoomModel(Long id, String suffix, String name, int capacity,
                     BuildingModel building, EquipmentModel[] equipment, ClosureModel closure) {
        this.id = id;
        this.suffix = suffix;
        this.name = name;
        this.capacity = capacity;
        this.building = building;
        this.equipment = equipment;
        this.closure = closure;
    }

    /**
     * Instantiates the {@link RoomModel} class without closure.
     *
     * @param id       Unique numeric identifier of the room
     * @param suffix   Room suffix
     * @param name     Room name
     * @param capacity Room maximal capacity
     * @param building The building information object
     */
    public RoomModel(Long id, String suffix, String name, int capacity,
                     BuildingModel building, EquipmentModel[] equipment) {
        this(id, suffix, name, capacity, building, equipment, null);
    }

    private RoomModel() {
        // default constructor for model materialization
    }

    /**
     * Gets the unique numeric identifier of the room.
     *
     * @return ID of the room
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the room suffix.
     *
     * @return Room suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Gets the name of the room.
     *
     * @return Room name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the maximal room capacity.
     *
     * @return Room capacity
     */
    public int getCapacity() {
        return capacity;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setBuilding(BuildingModel building) {
        this.building = building;
    }

    public void setEquipment(EquipmentModel[] equipment) {
        this.equipment = equipment;
    }

    public void setClosure(ClosureModel closure) {
        this.closure = closure;
    }

    /**
     * Gets the building information model.
     *
     * @return Building model
     */
    public BuildingModel getBuilding() {
        return building;
    }

    public Stream<EquipmentModel> getEquipment() {
        return Arrays.stream(equipment);
    }

    /**
     * Gets the room closure object (if any).
     *
     * @return Room closure (or null)
     */
    public ClosureModel getClosure() {
        return closure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomModel roomModel = (RoomModel) o;
        return Objects.equals(id, roomModel.id) && capacity == roomModel.capacity
                && suffix.equals(roomModel.suffix) && name.equals(roomModel.name)
                && building.equals(roomModel.building)
                && Arrays.equals(equipment, roomModel.equipment)
                && Objects.equals(closure, roomModel.closure);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, suffix, name, capacity, building, closure);
        result = 31 * result + Arrays.hashCode(equipment);
        return result;
    }

    @Override
    public String toString() {
        return "RoomModel{"
                + "id=" + id
                + ", suffix='" + suffix + '\''
                + ", name='" + name + '\''
                + ", capacity=" + capacity
                + ", building=" + building
                + ", equipment=" + Arrays.toString(equipment)
                + ", closure=" + closure
                + '}';
    }
}
