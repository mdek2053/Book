package nl.tudelft.sem11b.data.models;

public class IdModel<T> {
    private final T id;

    public IdModel(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }
}
