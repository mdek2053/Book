package nl.tudelft.sem11b.data.models;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;

public class PageData<T> {
    private final long total;
    private final List<T> data;

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

    public PageData(Page<T> page) {
        this(page.getTotalElements(), page.getContent());
    }

    public long getTotal() {
        return total;
    }

    public Stream<T> getData() {
        return data.stream();
    }
}
