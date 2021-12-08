package nl.tudelft.sem11b.data.models;

import java.io.Serializable;

public class BuildingObject implements Serializable {
    Long id;
    String prefix;
    String name;
    String open;
    String close;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public BuildingObject() {
    }

    /**
     * Creates a Building object.
     * @param id Unique numeric identifier of the building
     * @param prefix Alphanumeric prefix that uniquely identifies the building
     * @param name Name of the building (not necessarily unique)
     * @param open Opening hours of the building
     * @param close Closing hours of the building
     */
    public BuildingObject(Long id, String prefix, String name, String open, String close) {
        this.id = id;
        this.prefix = prefix;
        this.name = name;
        this.open = open;
        this.close = close;
    }
}
