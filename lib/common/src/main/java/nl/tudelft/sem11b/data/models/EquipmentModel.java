package nl.tudelft.sem11b.data.models;

public class EquipmentModel {
    private final long id;
    private final String name;

    public EquipmentModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
