package nl.tudelft.sem11b.data.models;

public class EquipmentModel {
    private final int id;
    private final String name;

    public EquipmentModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
