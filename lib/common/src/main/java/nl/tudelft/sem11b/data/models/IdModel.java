package nl.tudelft.sem11b.data.models;

/**
 * Represents the ID of newly created entity.
 *
 * @param <T> Type of ID
 */
public class IdModel<T> {
    private T id;

    /**
     * Instantiates the {@link IdModel} class.
     *
     * @param id ID of a newly created entity
     */
    public IdModel(T id) {
        this.id = id;
    }

    private IdModel() {
        // default constructor for model materialization
    }

    /**
     * Gets the ID of the newly created entity.
     *
     * @return ID of entity
     */
    public T getId() {
        return id;
    }
}
