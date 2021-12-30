package nl.tudelft.sem11b.data.models;

import java.util.Objects;

/**
 * Holds information about room equipment.
 */
public class EquipmentModel {
    private Long id;
    private String name;

    /**
     * Instantiates the {@link EquipmentModel} class.
     *
     * @param id   Unique numeric identifier of the equipment
     * @param name Name of the equipment
     */
    public EquipmentModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public EquipmentModel(String name) {
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
    public Long getId() {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EquipmentModel that = (EquipmentModel) o;
        return Objects.equals(id, that.id) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "EquipmentModel{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}
