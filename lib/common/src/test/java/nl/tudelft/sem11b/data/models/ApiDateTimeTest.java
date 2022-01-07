package nl.tudelft.sem11b.data.models;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.ApiTime;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApiDateTimeTest {

    ApiDate christmas = new ApiDate(2022, 12, 25);
    ApiTime ten = new ApiTime(10, 0);

    ApiDateTime christmasTen = new ApiDateTime(christmas, ten);
    ApiDateTime christmasTen2 = new ApiDateTime(christmas, ten);

    @Test
    void parseInvalidFormatTest() {
        assertThrows(ParseException.class, () -> ApiDateTime.parse("yeet"));
    }

    @Test
    void parseSuccessfulTest() throws ParseException {
        assertEquals(christmasTen, ApiDateTime.parse("2022-12-25T10:00"));
    }

    @Test
    void nullDateTest() {
        assertThrows(IllegalArgumentException.class, () -> new ApiDateTime(null, ten));
    }

    @Test
    void nullTimeTest() {
        assertThrows(IllegalArgumentException.class, () -> new ApiDateTime(christmas, null));
    }

    @Test
    void equalsNullTest() {
        assertFalse(christmasTen.equals(null));
    }

    @Test
    void equalsDifferentClassTest() {
        assertFalse(christmasTen.equals(" "));
    }

    @Test
    void equalsSameDateTest() {
        assertEquals(christmasTen, christmasTen2);
    }
}
