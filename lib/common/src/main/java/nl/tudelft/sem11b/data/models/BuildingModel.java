package nl.tudelft.sem11b.data.models;

import nl.tudelft.sem11b.data.TimeOfDay;

public class BuildingModel {
    private final int id;
    private final String prefix;
    private final String name;
    private final TimeOfDay open;
    private final TimeOfDay close;

    public BuildingModel(int id, String prefix, String name, TimeOfDay open , TimeOfDay close) {
        this.id = id;
        this.prefix = prefix;
        this.name = name;
        this.open = open;
        this.close = close;
    }

    public long getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public TimeOfDay getOpen() {
        return open;
    }

    public TimeOfDay getClose() {
        return close;
    }
}
