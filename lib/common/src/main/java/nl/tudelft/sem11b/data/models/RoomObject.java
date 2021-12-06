package nl.tudelft.sem11b.data.models;

import java.io.Serializable;

public class RoomObject implements Serializable {
    Long id;
    String suffix;
    String name;
    BuildingObject building;
    Integer capacity;
    EquipmentObject[] equipment;
    ClosureObject closure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BuildingObject getBuilding() {
        return building;
    }

    public void setBuilding(BuildingObject building) {
        this.building = building;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public EquipmentObject[] getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentObject[] equipment) {
        this.equipment = equipment;
    }

    public ClosureObject getClosure() {
        return closure;
    }

    public void setClosure(ClosureObject closure) {
        this.closure = closure;
    }

    public RoomObject() {
    }

    /**
     * Creates a Room object.
     * @param id Unique numeric identifier of a room
     * @param suffix Alphanumeric suffix that uniquely identifies the room within a building
     * @param name Name of the room (not necessarily unique)
     * @param building Building the room is part of
     * @param capacity Maximal capacity of the room
     * @param equipment Room's equipment
     * @param closure null if room is not closed, otherwise holds info about closure
     */
    public RoomObject(Long id, String suffix, String name, BuildingObject building,
                      Integer capacity, EquipmentObject[] equipment,
                      ClosureObject closure) {
        this.id = id;
        this.suffix = suffix;
        this.name = name;
        this.building = building;
        this.capacity = capacity;
        this.equipment = equipment;
        this.closure = closure;
    }
}
