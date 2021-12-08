package nl.tudelft.sem11b.data.models;

import java.io.Serializable;

public class ClosureObject implements Serializable {
    String description;
    String since;
    String until;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public ClosureObject() {
    }

    /**
     * Creates a Closure object.
     * @param description Reason why room is not available
     * @param since Date at which the closure will begin
     * @param until Date at which the closure will end
     */
    public ClosureObject(String description, String since, String until) {
        this.description = description;
        this.since = since;
        this.until = until;
    }

    /**
     * Checks if the entity has all mandatory fields filled in.
     * @return true if valid, false otherwise
     */
    public boolean validate() {
        return description != null && description.length() > 0
                && since != null && since.length() > 0 // this should be changed to
                && until != null && until.length() > 0; // date string validation once feasible.
    }
}
