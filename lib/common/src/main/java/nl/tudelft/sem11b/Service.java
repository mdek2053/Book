package nl.tudelft.sem11b;

public class Service {
    private final String name;

    public Service(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String[] buildSpringArgs(String[] args) {
        return args;
    }
}
