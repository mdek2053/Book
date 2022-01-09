package nl.tudelft.sem11b.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

public class ApiTimeTests {
    @Test
    public void parseTest() {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j++) {
                ApiTime parsed;
                ApiTime formatted;
                try {
                    parsed = ApiTime.parse(String.format("%d:%d", i, j));
                    formatted = ApiTime.parse(parsed.toString());
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                assertEquals(i, parsed.getHour());
                assertEquals(j, parsed.getMinute());
                assertEquals(parsed, formatted);
            }
        }
    }

    @Test
    public void outOfBoundsTest() {
        assertThrows(ParseException.class, () -> ApiTime.parse("24:00"));
        assertThrows(IllegalArgumentException.class, () -> new ApiTime(24, 0));

        assertThrows(ParseException.class, () -> ApiTime.parse("-1:00"));
        assertThrows(IllegalArgumentException.class, () -> new ApiTime(-1, 0));

        assertThrows(ParseException.class, () -> ApiTime.parse("00:60"));
        assertThrows(IllegalArgumentException.class, () -> new ApiTime(0, 60));

        assertThrows(ParseException.class, () -> ApiTime.parse("00:-1"));
        assertThrows(IllegalArgumentException.class, () -> new ApiTime(0, -1));
    }

    @Test
    public void limitTest() {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j++) {
                var time = new ApiTime(i, j);
                assertTrue(ApiTime.MINIMUM.compareTo(time) <= 0);
                assertTrue(ApiTime.MAXIMUM.compareTo(time) >= 0);
            }
        }
    }

}
