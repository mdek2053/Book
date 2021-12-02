package nl.tudelft.sem11b.data.models;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Represents a zero-based page index.
 */
public class PageIndex {
    private final int index;
    private final int limit;

    /**
     * Instantiates the {@link PageIndex} class.
     *
     * @param idx Index of page
     * @param lim Size of pages
     */
    public PageIndex(int idx, int lim) {
        if (idx < 0 || lim < 1) {
            throw new IllegalArgumentException();
        }

        index = idx;
        limit = lim;
    }

    /**
     * Gets the zero-based page index.
     *
     * @return Page index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the maximal size of a page.
     *
     * @return Maximal size of a page
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the total zero-based offset in the whole collection of items.
     *
     * @return Total offset
     */
    public int getOffset() {
        return index * limit;
    }

    /**
     * Creates a JPA paging object equivalent to this page index.
     *
     * @return JPA paging object
     */
    public Pageable getPage() {
        return PageRequest.of(index, limit);
    }

    /**
     * Creates a JPA paging object equivalent to this page index.
     *
     * @param sort Order of items
     * @return JPA paging object
     */
    public Pageable getPage(Sort sort) {
        return PageRequest.of(index, limit, sort);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        PageIndex that = (PageIndex) other;
        return index == that.index && limit == that.limit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, limit);
    }

    /**
     * Constructs a {@link PageIndex} from URL query parameters. Preforms limit checks to ensure the
     * parameters are valid.
     *
     * @param page  Page index
     * @param limit Size of pages
     * @return Page index representative of the query parameters
     * @throws ResponseStatusException When query parameters are invalid
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static PageIndex fromQuery(Optional<Integer> page, Optional<Integer> limit) {
        if (page.map(i -> i < 0).orElse(false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Page query parameter must be a non-negative integer!");
        }

        if (limit.map(i -> i < 1 || i > 128).orElse(false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Limit query parameter must be an integer between 1 and 128 (inclusive)!");
        }

        return new PageIndex(page.orElse(0), limit.orElse(32));
    }

}
