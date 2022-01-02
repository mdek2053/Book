package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Test;

class PageDataTest {

    PageData pageData = new PageData<>(2, Arrays.asList(1L, 2L));

    @Test
    void testNegativeTotal() {
        assertThrows(IllegalArgumentException.class,
                () -> new PageData<Long>(-1, new ArrayList<>()));
    }

    @Test
    void testDataNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new PageData<Long>(1, null));
    }

    @Test
    void testTotalTooBig() {
        assertThrows(IllegalArgumentException.class,
                () -> new PageData<>(1, Arrays.asList(1L, 2L)));
    }

    @Test
    void testSameEqual() {
        assertEquals(pageData, pageData);
    }

    @Test
    void testNotInstance() {
        assertFalse(pageData.equals(new Object()));
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hashCode(pageData), pageData.hashCode());
    }
}