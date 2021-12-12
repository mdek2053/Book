package nl.tudelft.sem11b.admin.data.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;

/**
 * Represents a single room of a {@link Building}.
 */
@Entity
@Table(indexes = {@Index(columnList = "suffix, building_id", unique = true)})
public class Room {
    @Id @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "suffix", nullable = false)
    private String suffix;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "capacity", nullable = false)
    private int capacity;
    @Embedded
    private Closure closure;

    @JoinColumn(name = "building_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Building building;

    /**
     * Gets the unique numeric identifier of the room.
     *
     * @return ID of the room
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the suffix of the room.
     *
     * @return Room suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets a new room suffix.
     *
     * @param suffix New room suffix
     */
    public void setPrefix(String suffix) {
        if (suffix == null || suffix.trim().isEmpty()) {
            throw new IllegalArgumentException("Suffix may not be empty nor null!");
        }

        this.suffix = suffix.trim();
    }

    /**
     * Gets the name of the room.
     *
     * @return Name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the room.
     *
     * @param name New name of the room
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name may not be empty nor null!");
        }

        this.name = name.trim();
    }

    /**
     * Gets the entity of the parent building.
     *
     * @return Parent building
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Converts the entity into its equivalent stud (partial) model.
     *
     * @return Stud model
     */
    public RoomStudModel toStudModel() {
        return new RoomStudModel(id, suffix, name, capacity,
            closure == null ? null : closure.toModel());
    }

    /**
     * Converts the entity into its equivalent model.
     *
     * @return Room model
     */
    public RoomModel toModel() {
        return new RoomModel(id, suffix, name, capacity,
            building.toModel(), closure == null ? null : closure.toModel());
    }

    public int getCapacity() {
        return capacity;
    }
}
