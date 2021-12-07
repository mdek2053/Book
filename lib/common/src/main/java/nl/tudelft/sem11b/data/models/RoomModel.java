package nl.tudelft.sem11b.data.models;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Holds all room information.
 */
public class RoomModel {
    private final int id;
    private final String suffix;
    private final String name;
    private final int capacity;
    private final BuildingModel building;
    private final EquipmentModel[] equipment;
    private final ClosureModel closure;

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
    public RoomModel(int id, String suffix, String name, int capacity,
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
    public RoomModel(int id, String suffix, String name, int capacity,
                     BuildingModel building, EquipmentModel[] equipment) {
        this(id, suffix, name, capacity, building, equipment, null);
    }

    /**
     * Gets the unique numeric identifier of the room.
     *
     * @return ID of the room
     */
    public int getId() {
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
}
