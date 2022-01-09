package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

class PageIndexTest {

    PageIndex pageIndex = new PageIndex(0, 10);

    @Test
    void testInvalidConstructorNegativeIndex() {
        assertThrows(IllegalArgumentException.class, () -> new PageIndex(-1, 10));
    }

    @Test
    void testInvalidConstructorLimitUnder1() {
        assertThrows(IllegalArgumentException.class, () -> new PageIndex(1, 0));
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
    void testEqualsNull() {
        assertNotEquals(pageIndex, null);
    }

    @Test
    void testEqualsDifferentClass() {
        assertNotEquals(pageIndex, " ");
    }

    @Test
    void testEqualsDifferentIndex() {
        assertNotEquals(pageIndex, new PageIndex(1, 10));
    }

    @Test
    void testEqualsDifferentLimit() {
        assertNotEquals(pageIndex, new PageIndex(0, 11));
    }

    @Test
    void testEqualsSuccessful() {
        assertEquals(pageIndex, new PageIndex(0, 10));
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

    @Test
    void testFromQueryLimitOver128() {
        assertThrows(ResponseStatusException.class,
                () -> PageIndex.fromQuery(Optional.of(1), Optional.of(129)));
    }

    @Test
    void testFromQuerySuccessful() {
        assertEquals(pageIndex, PageIndex.fromQuery(Optional.of(0), Optional.of(10)));
    }
}