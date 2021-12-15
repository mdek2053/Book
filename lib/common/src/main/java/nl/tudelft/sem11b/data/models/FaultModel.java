package nl.tudelft.sem11b.data.models;

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
}
