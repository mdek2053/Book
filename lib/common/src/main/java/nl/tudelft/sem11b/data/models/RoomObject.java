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

    public RoomObject(Long id, String suffix, String name, BuildingObject building, Integer capacity, EquipmentObject[] equipment, ClosureObject closure) {
        this.id = id;
        this.suffix = suffix;
        this.name = name;
        this.building = building;
        this.capacity = capacity;
        this.equipment = equipment;
        this.closure = closure;
    }
}
