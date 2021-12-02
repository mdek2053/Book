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

    public ClosureObject(String description, String since, String until) {
        this.description = description;
        this.since = since;
        this.until = until;
    }
}
