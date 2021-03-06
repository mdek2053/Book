package nl.tudelft.sem11b.data.models;

import java.util.Objects;

/**
 * Holds information about a reported room fault.
 */
public class FaultModel {
    private long id;
    private long reporter;
    private String description;
    private long roomId;

    /**
     * Instantiates the {@link FaultModel} class.
     *
     * @param id          Unique identifier of the fault report
     * @param reporter    ID of the user who reported the fault
     * @param description Description of the fault
     * @param roomId      ID of the room which needs maintenance
     */
    public FaultModel(long id, long reporter, String description, long roomId) {
        this.id = id;
        this.reporter = reporter;
        this.description = description;
        this.roomId = roomId;
    }

    private FaultModel() {
        // default constructor for model materialization
    }

    public long getId() {
        return id;
    }

    public long getReporter() {
        return reporter;
    }

    public String getDescription() {
        return description;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setReporter(long reporter) {
        this.reporter = reporter;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FaultModel)) {
            return false;
        }

        FaultModel that = (FaultModel) o;

        if (id != that.id) {
            return false;
        }
        if (reporter != that.reporter) {
            return false;
        }
        if (roomId != that.roomId) {
            return false;
        }
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (reporter ^ (reporter >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (int) (roomId ^ (roomId >>> 32));
        return result;
    }
}
