package nl.tudelft.sem11b.admin.data.entities;

import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.data.models.EquipmentModel;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;


/**
 * Represents a single room of a {@link Building}.
 */
@Entity
@Table(indexes = {@Index(columnList = "suffix, building_id", unique = true)})
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "suffix", nullable = false)
    private String suffix;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "capacity", nullable = false)
    private int capacity;
    @Embedded
    private Closure closure;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable
    private Set<Equipment> equipment;

    public Room() {

    }

    @JoinColumn(name = "building_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Building building;

    /**
     * Gets the unique numeric identifier of the room.
     *
     * @return ID of the room
     */
    public Long getId() {
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
     * Sets the room closed.
     *
     * @param closure Closure object
     */
    public void setClosure(Closure closure) {
        this.closure = closure;
    }

    /**
     * Gets the closure..
     *
     * @return Closure object
     */
    public Closure getClosure() {
        return closure;
    }

    /**
     * Gets the entity of the parent building.
     *
     * @return Parent building
     */
    public Building getBuilding() {
        return building;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(Set<Equipment> equipment) {
        this.equipment = equipment;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void addEquipment(Equipment e) {
        equipment.add(e);
    }

    /**
     * Constructs a room object.
     *
     * @param id        the room's id
     * @param suffix    the suffix of the room
     * @param name      name of the room
     * @param capacity  capacity
     * @param closure   object which specifies the closure, or null if open
     * @param building  object representing the building the room is part of
     */
    public Room(long id, String suffix, String name, int capacity,
                Closure closure, Building building, Set<Equipment> equipment) {
        this.id = id;
        this.suffix = suffix;
        this.name = name;
        this.capacity = capacity;
        this.closure = closure;
        this.building = building;
        this.equipment = equipment;
    }

    /**
     * Constructs a room object with id null.
     *
     * @param suffix    the suffix of the room
     * @param name      name of the room
     * @param capacity  capacity
     * @param closure   object which specifies the closure, or null if open
     * @param building  object representing the building the room is part of
     */
    public Room(String suffix, String name, int capacity,
                Closure closure, Building building, Set<Equipment> equipment) {
        this.suffix = suffix;
        this.name = name;
        this.capacity = capacity;
        this.closure = closure;
        this.building = building;
        this.equipment = equipment;
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
        EquipmentModel[] equipmentModels = equipment.stream().map(x -> x.toModel())
                .toArray(EquipmentModel[]::new);
        return new RoomModel(id, suffix, name, capacity,
            building.toModel(), equipmentModels, closure == null ? null : closure.toModel());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id) && capacity == room.capacity
                && suffix.equals(room.suffix) && name.equals(room.name)
                && Objects.equals(closure, room.closure)
                && equipment.equals(room.equipment) && building.equals(room.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, suffix, name, capacity, closure);
    }

    @Override
    public String toString() {
        return "Room{"
                + "id=" + id
                + ", suffix='" + suffix + '\''
                + ", name='" + name + '\''
                + ", capacity=" + capacity
                + ", closure=" + closure
                + ", equipment=" + equipment
                + ", building=" + building
                + '}';

    }
}
