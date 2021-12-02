package nl.tudelft.sem11b.data.models;

import java.io.Serializable;

public class EquipmentObject implements Serializable {
    Long id;
    String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EquipmentObject() {
    }

    public EquipmentObject(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
