package nl.tudelft.sem11b.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

public class TimeOfDayTests {
    @Test
    public void parseTest() {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j++) {
                TimeOfDay parsed;
                TimeOfDay formatted;
                try {
                    parsed = TimeOfDay.parse(String.format("%d:%d", i, j));
                    formatted = TimeOfDay.parse(parsed.toString());
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
        assertThrows(ParseException.class, () -> TimeOfDay.parse("24:00"));
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(24, 0));

        assertThrows(ParseException.class, () -> TimeOfDay.parse("-1:00"));
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(-1, 0));

        assertThrows(ParseException.class, () -> TimeOfDay.parse("00:60"));
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(0, 60));

        assertThrows(ParseException.class, () -> TimeOfDay.parse("00:-1"));
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(0, -1));
    }

    @Test
    public void limitTest() {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j++) {
                var time = new TimeOfDay(i, j);
                assertTrue(TimeOfDay.MINIMUM.compareTo(time) <= 0);
                assertTrue(TimeOfDay.MAXIMUM.compareTo(time) >= 0);
            }
        }
    }
}
