package nl.tudelft.sem11b.data.models;

public class EquipmentModel {
    private long id;
    private String name;

    public EquipmentModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    private EquipmentModel() {

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
