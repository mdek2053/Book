package nl.tudelft.sem11b.data.models;

/**
 * Holds information about room equipment.
 */
public class EquipmentModel {
    private long id;
    private String name;

    /**
     * Instantiates the {@link EquipmentModel} class.
     *
     * @param id   Unique numeric identifier of the equipment
     * @param name Name of the equipment
     */
    public EquipmentModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    private EquipmentModel() {
        // default constructor for model materialization
    }

    /**
     * Gets the unique numeric identifier of the equipment.
     *
     * @return ID of the equipment
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of equipment.
     *
     * @return Name of equipment
     */
    public String getName() {
        return name;
    }
}