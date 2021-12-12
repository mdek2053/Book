package nl.tudelft.sem11b.data.models;

public class IdModel<T> {
    private T id;

    public IdModel(T id) {
        this.id = id;
    }

    private IdModel() {}

    public T getId() {
        return id;
    }
}
