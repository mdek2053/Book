package nl.tudelft.sem11b.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

public class ApiDateTest {

    ApiDate christmas = new ApiDate(2022, 12, 25);
    ApiDate marchSecond = new ApiDate(2022, 3, 2);
    ApiDate marchSecondLeapYear = new ApiDate(2024, 3, 2);

    @Test
    void getDayNonLeapYearTest() {
        assertEquals(2, marchSecond.getDay());
    }

    @Test
    void getDayLeapYearTest() {
        assertEquals(2, marchSecondLeapYear.getDay());
    }

    @Test
    void getMonthNonLeapYearTest() {
        assertEquals(3, marchSecond.getMonth());
    }

    @Test
    void getMonthLeapYearTest() {
        assertEquals(3, marchSecondLeapYear.getMonth());
    }

    @Test
    void monthBelow1Test() {
        assertThrows(IllegalArgumentException.class, () -> new ApiDate(2022, 0, 10));
    }

    @Test
    void monthOver12Test() {
        assertThrows(IllegalArgumentException.class, () -> new ApiDate(2022, 13, 10));
    }

    @Test
    void dayBelow1Test() {
        assertThrows(IllegalArgumentException.class, () -> new ApiDate(2022, 12, 0));
    }

    @Test
    void dayTooHighTest() {
        assertThrows(IllegalArgumentException.class, () -> new ApiDate(2022, 4, 31));
    }

    @Test
    void setDayBelow1Test() {
        assertThrows(IllegalArgumentException.class, () -> christmas.setDay(0));
    }

    @Test
    void setDayOver365Test() {
        assertThrows(IllegalArgumentException.class, () -> christmas.setDay(366));
    }

    @Test
    void equalsNullTest() {
        assertFalse(christmas.equals(null));
    }

    @Test
    void equalsDifferentClassTest() {
        assertFalse(christmas.equals(" "));
    }
}
