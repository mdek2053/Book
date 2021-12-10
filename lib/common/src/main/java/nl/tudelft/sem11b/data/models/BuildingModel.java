package nl.tudelft.sem11b.data.models;

import nl.tudelft.sem11b.data.ApiTime;

/**
 * Holds all building information.
 */
public class BuildingModel {
    private final long id;
    private final String prefix;
    private final String name;
    private final ApiTime open;
    private final ApiTime close;

    /**
     * Instantiates the {@link BuildingModel}.
     *
     * @param id Unique numeric identifier of the building
     * @param prefix Building prefix
     * @param name Building name
     * @param open Time of day at which the building opens
     * @param close This of day at which the building closes
     */
    public BuildingModel(long id, String prefix, String name, ApiTime open, ApiTime close) {
        this.id = id;
        this.prefix = prefix;
        this.name = name;
        this.open = open;
        this.close = close;
    }

    /**
     * Gets the unique numeric identifier of the building.
     *
     * @return ID of the building
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the prefix of the building.
     *
     * @return Prefix of the building
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets the name of the building.
     *
     * @return Name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the opening time of the building.
     *
     * @return Opening time of the building
     */
    public ApiTime getOpen() {
        return open;
    }

    /**
     * Gets the closing time of the building.
     *
     * @return Closing time of the building
     */
    public ApiTime getClose() {
        return close;
    }
}
