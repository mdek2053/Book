package nl.tudelft.sem11b.admin.data;

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

    /**
     * Converts the closure entity into its equivalent model.
     *
     * @return Closure model
     */
    public ClosureModel toModel() {
        // TODO: unify this class and the model class (so move this class into common lib)
        return new ClosureModel(reason, since, until);
    }
}
