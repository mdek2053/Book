package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

class PageIndexTest {

    PageIndex pageIndex = new PageIndex(0, 10);

    @Test
    void testInvalidConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new PageIndex(-1, 10));
    }

    @Test
    void testGetIndex() {
        assertEquals(0, pageIndex.getIndex());
    }

    @Test
    void testGetLimit() {
        assertEquals(10, pageIndex.getLimit());
    }

    @Test
    void testGetOffset() {
        assertEquals(0, pageIndex.getOffset());
    }

    @Test
    void testGetPage() {
        assertEquals(PageRequest.of(0, 10), pageIndex.getPage());
    }

    @Test
    void testSameEqual() {
        assertEquals(pageIndex, pageIndex);
    }

    @Test
    void testNotInstance() {
        assertFalse(pageIndex.equals(new Object()));
    }

    @Test
    void testFromQueryEmptyPage() {
        assertThrows(ResponseStatusException.class,
                () -> PageIndex.fromQuery(Optional.of(-1), Optional.of(10)));
    }

    @Test
    void testFromQueryEmptyLimit() {
        assertThrows(ResponseStatusException.class,
                () -> PageIndex.fromQuery(Optional.of(1), Optional.of(-6)));
    }
}