package nl.tudelft.sem11b.data.models;

/**
 * A summary object for a room used in listings.
 */
public class FaultStudModel {
    private long id;
    private long reporter;
    private String description;

    /**
     * Instantiates the {@link FaultStudModel} class.
     *
     * @param id          Unique identifier of the fault
     * @param reporter    ID of the user who made the report
     * @param description Description of the fault
     */
    public FaultStudModel(long id, long reporter, String description) {
        this.id = id;
        this.reporter = reporter;
        this.description = description;
    }

    private FaultStudModel() {
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
}
