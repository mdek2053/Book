package nl.tudelft.sem11b.admin.data.entities;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.models.BuildingModel;

/**
 * Represents a single building.
 */
@Entity
public class Building {
    @Id @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "prefix", nullable = false, unique = true)
    private String prefix;
    @Column(name = "name", nullable = false)
    private String name;
    @Embedded
    // Undeniable proof that JPA is trash. This is required because JPA naming strategy will not
    // affix the embedded column names
    @AttributeOverrides({
        @AttributeOverride(name = "timestamp", column = @Column(name = "opening_timestamp"))
    })
    private ApiTime opening;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "timestamp", column = @Column(name = "closing_timestamp"))
    })
    private ApiTime closing;

    @OneToMany(mappedBy = "building")
    private Set<Room> rooms;

    public Building() {

    }

    /**
     * Gets the unique numeric identifier of the building.
     *
     * @return ID of the building
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the building prefix.
     *
     * @return Building prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the building prefix.
     *
     * @param prefix New building prefix
     */
    public void setPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Prefix may not be empty nor null!");
        }

        this.prefix = prefix.trim();
    }

    /**
     * Gets the name of the building.
     *
     * @return Name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the building.
     *
     * @param name New building name
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name may not be empty nor null!");
        }

        this.name = name.trim();
    }

    /**
     * Gets the opening time of the building.
     *
     * @return Opening time of the building
     */
    public ApiTime getOpening() {
        return opening;
    }

    /**
     * Sets opening time of the building. Opening hours are checked to be consistent with closing
     * hours. In case of a discrepancy, an {@link IllegalArgumentException} is thrown.
     *
     * @param opening New opening time of the building
     */
    public void setOpening(ApiTime opening) {
        setHours(opening, closing);
    }

    /**
     * Gets the closing time of the building.
     *
     * @return Closing time of the building
     */
    public ApiTime getClosing() {
        return closing;
    }

    /**
     * Sets closing time of the building. Closing hours are checked to be consistent with opening
     * hours. In case of a discrepancy, an {@link IllegalArgumentException} is thrown.
     *
     * @param closing New closing time of the building
     */
    public void setClosing(ApiTime closing) {
        setHours(opening, closing);
    }

    /**
     * Sets the opening and closing hours at once. The given hours are checked to be consistent. In
     * case of a discrepancy, and {@link IllegalArgumentException} is thrown.
     *
     * @param opening New opening hours
     * @param closing New closing hours
     */
    public void setHours(ApiTime opening, ApiTime closing) {
        if (opening.compareTo(closing) >= 0) {
            throw new IllegalArgumentException("Opening hours must be before closing hours!");
        }

        this.opening = opening;
        this.closing = closing;
    }

    /**
     * Adds a new room to the building.
     *
     * @param room Room to add
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    /**
     * Lists all the rooms in this building.
     *
     * @return Rooms in this building
     */
    public Stream<Room> getRooms() {
        return rooms.stream();
    }

    /**
     * Creates a building object.
     *
     * @param id       building's id
     * @param prefix   prefix of building
     * @param name     name of building
     * @param opening  time the building opens
     * @param closing  time the building closes
     * @param rooms    rooms contained in the building
     */
    public Building(long id, String prefix, String name,
                    ApiTime opening, ApiTime closing, Set<Room> rooms) {
        this.id = id;
        this.prefix = prefix;
        this.name = name;
        this.opening = opening;
        this.closing = closing;
        this.rooms = rooms;
    }

    /**
     * Converts the building entity into its equivalent model.
     *
     * @return Building model
     */
    public BuildingModel toModel() {
        return new BuildingModel(id, prefix, name, opening, closing);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Building building = (Building) o;
        return id == building.id && Objects.equals(prefix, building.prefix)
                && Objects.equals(name, building.name)
                && Objects.equals(opening, building.opening)
                && Objects.equals(closing, building.closing)
                && Objects.equals(rooms, building.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prefix, name, opening, closing, rooms);
    }
}
