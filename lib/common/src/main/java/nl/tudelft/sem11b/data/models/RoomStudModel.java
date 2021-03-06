package nl.tudelft.sem11b.data.models;

import java.util.Objects;

/**
 * A summary object for a room used in listings.
 */
public class RoomStudModel {
    private long id;
    private String suffix;
    private String name;
    private int capacity;
    private ClosureModel closure;

    /**
     * Instantiates the {@link RoomStudModel} class.
     *
     * @param id       Unique numeric identifier of the room
     * @param suffix   Room suffix
     * @param name     Room name
     * @param capacity Maximal room capacity
     * @param closure  Room closure object (if any)
     */
    public RoomStudModel(long id, String suffix, String name, int capacity, ClosureModel closure) {
        this.id = id;
        this.suffix = suffix;
        this.name = name;
        this.capacity = capacity;
        this.closure = closure;
    }

    /**
     * Instantiates the {@link RoomStudModel} class.
     *
     * @param id       Unique numeric identifier of the room
     * @param suffix   Room suffix
     * @param name     Room name
     * @param capacity Maximal room capacity
     */
    public RoomStudModel(long id, String suffix, String name, int capacity) {
        this(id, suffix, name, capacity, null);
    }

    private RoomStudModel() {
        // default constructor for model materialization
    }

    /**
     * Gets the unique numeric identifier of the room.
     *
     * @return ID of the room
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the room suffix.
     *
     * @return Room suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Gets the name of the room.
     *
     * @return Room name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the maximal room capacity.
     *
     * @return Room capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the room closure object (if any).
     *
     * @return Room closure (or null)
     */
    public ClosureModel getClosure() {
        return closure;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setClosure(ClosureModel closure) {
        this.closure = closure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomStudModel that = (RoomStudModel) o;
        return id == that.id && capacity == that.capacity
                && suffix.equals(that.suffix) && name.equals(that.name)
                && Objects.equals(closure, that.closure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, suffix, name, capacity, closure);
    }

}
