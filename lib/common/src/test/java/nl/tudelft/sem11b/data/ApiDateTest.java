package nl.tudelft.sem11b.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

public class ApiDateTest {

    ApiDate christmas = new ApiDate(2022, 12, 25);
    ApiDate marchSecond = new ApiDate(2022, 3, 2);
    ApiDate marchThird = new ApiDate(2022, 3, 3);
    ApiDate marchFourth = new ApiDate(2022, 3, 4);
    ApiDate marchSecondLeapYear = new ApiDate(2024, 3, 2);

    ApiDate lastDayOf2022 = new ApiDate(2022, 12, 31);
    ApiDate firstDayOf2023 = new ApiDate(2023, 1, 1);

    ApiDate lastDayOf2024 = new ApiDate(2024, 12, 31);
    ApiDate firstDayOf2025 = new ApiDate(2025, 1, 1);

    @Test
    void parseInvalidFormatTest() {
        assertThrows(ParseException.class, () -> ApiDate.parse("chonky"));
    }

    @Test
    void parseInvalidDateTest() {
        assertThrows(ParseException.class, () -> ApiDate.parse("2022-13-13"));
    }

    @Test
    void parseSuccessfulTest() throws ParseException {
        assertEquals(christmas, ApiDate.parse("2022-12-25"));
    }

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
    void afterTest1() {
        assertEquals(marchThird, marchSecond.after());
    }

    @Test
    void beforeTest1() {
        assertEquals(marchSecond, marchThird.before());
    }

    @Test
    void afterTestYearChange() {
        assertEquals(firstDayOf2023, lastDayOf2022.after());
    }

    @Test
    void afterTestLeapYearChange() {
        assertEquals(firstDayOf2025, lastDayOf2024.after());
    }

    @Test
    void beforeTestYearChange() {
        assertEquals(lastDayOf2022, firstDayOf2023.before());
    }

    @Test
    void afterTest2() {
        assertEquals(marchFourth, marchSecond.after(2));
    }

    @Test
    void beforeTest2() {
        assertEquals(marchSecond, marchFourth.before(2));
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
