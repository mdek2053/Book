package nl.tudelft.sem11b.data.models;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;

/**
 * Represents a page of items.
 *
 * @param <T> Type of items
 */
public class PageData<T> {
    private final long total;
    private final List<T> data;

    /**
     * Instantiates the {@link PageData} class.
     *
     * @param total Total number of items that match the query
     * @param data Page items
     */
    public PageData(long total, List<T> data) {
        if (total < 0) {
            throw new IllegalArgumentException("Total must be a non-negative integer!");
        }

        if (data == null) {
            throw new IllegalArgumentException("Data may not be null!");
        }

        if (total < data.size()) {
            throw new IllegalArgumentException(
                "Page cannot be bigger than the total number of items!");
        }

        this.total = total;
        this.data = data;
    }

    /**
     * Instantiates the {@link PageData} class.
     *
     * @param page JPA page
     */
    public PageData(Page<T> page) {
        this(page.getTotalElements(), page.getContent());
    }

    /**
     * Gets the total number of items that match the search criteria.
     *
     * @return Total number of items
     */
    public long getTotal() {
        return total;
    }

    /**
     * Gets the items of the page.
     *
     * @return Page items
     */
    public Stream<T> getData() {
        return data.stream();
    }
}
