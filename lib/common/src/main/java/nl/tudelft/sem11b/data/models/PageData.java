package nl.tudelft.sem11b.data.models;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;

/**
 * Represents a page of items.
 *
 * @param <T> Type of items
 */
public class PageData<T> {
    private long total;
    private List<T> data;

    /**
     * Instantiates the {@link PageData} class.
     *
     * @param total Total number of items that match the query
     * @param data  Page items
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

    private PageData() {
        // default constructor for model materialization
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

    public void setTotal(long total) {
        this.total = total;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PageData<?> pageData = (PageData<?>) o;
        return total == pageData.total && data.equals(pageData.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, data);
    }

}
