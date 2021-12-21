package nl.tudelft.sem11b.admin.data;

import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.models.ClosureModel;

/**
 * Represents a room closure.
 */
@Embeddable
public class Closure {
    @Column(name = "reason")
    private String reason;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "year", column = @Column(name = "since_year")),
        @AttributeOverride(name = "day", column = @Column(name = "since_da"))
    })
    private ApiDate since;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "year", column = @Column(name = "until_year")),
        @AttributeOverride(name = "day", column = @Column(name = "until_day"))
    })
    private ApiDate until;

    /**
     * Instantiates the {@link Closure} class.
     *
     * @param reason Reason for closure
     * @param since Beginning date of closure
     */
    public Closure(String reason, ApiDate since) {
        this(reason, since, null);
    }

    /**
     * Instantiates the {@link Closure} class.
     *
     * @param reason Reason for closure
     * @param since Beginning date of closure
     * @param until Ending date of closure
     */
    public Closure(String reason, ApiDate since, ApiDate until) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Reason must be given!");
        }
        if (since == null) {
            throw new IllegalArgumentException("Since date cannot be null!");
        }

        this.reason = reason;
        this.since = since;
        this.until = until;
    }

    private Closure() {
        // default constructor for entity materialization
    }

    /**
     * Gets the reason for room closure.
     *
     * @return Reason for closure
     */
    public String getReason() {
        return reason;
    }

    /**
     * Gets the beginning date of the closure.
     *
     * @return Beginning date of closure
     */
    public ApiDate getSince() {
        return since;
    }

    /**
     * Gets the ending date of the closure.
     *
     * @return Ending date of closure
     */
    public ApiDate getUntil() {
        return until;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSince(ApiDate since) {
        this.since = since;
    }

    public void setUntil(ApiDate until) {
        this.until = until;
    }

    /**
     * Converts the closure entity into its equivalent model.
     *
     * @return Closure model
     */
    public ClosureModel toModel() {
        return new ClosureModel(reason, since, until);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Closure)) {
            return false;
        }

        Closure closure = (Closure) o;

        if (!Objects.equals(reason, closure.reason)) {
            return false;
        }
        if (!Objects.equals(since, closure.since)) {
            return false;
        }
        return Objects.equals(until, closure.until);
    }

    @Override
    public int hashCode() {
        int result = reason != null ? reason.hashCode() : 0;
        result = 31 * result + (since != null ? since.hashCode() : 0);
        result = 31 * result + (until != null ? until.hashCode() : 0);
        return result;
    }
}
