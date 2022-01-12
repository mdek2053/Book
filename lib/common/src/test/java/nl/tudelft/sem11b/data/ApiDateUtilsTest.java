package nl.tudelft.sem11b.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;

import org.junit.jupiter.api.Test;



public class ApiDateUtilsTest {

    ApiDate christmas = new ApiDate(2022, 12, 25);
    ApiDate marchThird = new ApiDate(2022, 3, 3);
    ApiDate marchFourth = new ApiDate(2022, 3, 4);

    ApiDate lastDayOf2022 = new ApiDate(2022, 12, 31);
    ApiDate firstDayOf2023 = new ApiDate(2023, 1, 1);

    ApiDate lastDayOf2024 = new ApiDate(2024, 12, 31);
    ApiDate firstDayOf2025 = new ApiDate(2025, 1, 1);

    ApiDate marchSecond = new ApiDate(2022, 3, 2);
    ApiDate marchSecondLeapYear = new ApiDate(2024, 3, 2);

    @Test
    void parseInvalidFormatTest() {
        assertThrows(ParseException.class, () -> ApiDateUtils.parse("chonky"));
    }

    @Test
    void parseInvalidDateTest() {
        assertThrows(ParseException.class, () -> ApiDateUtils.parse("2022-13-13"));
    }

    @Test
    void parseSuccessfulTest() throws ParseException {
        assertEquals(christmas, ApiDateUtils.parse("2022-12-25"));
    }

    @Test
    void afterTest1() {
        assertEquals(marchThird, ApiDateUtils.after(marchSecond));
    }

    @Test
    void beforeTest1() {
        assertEquals(marchSecond, ApiDateUtils.before(marchThird));
    }

    @Test
    void afterTestYearChange() {
        assertEquals(firstDayOf2023, ApiDateUtils.after(lastDayOf2022));
    }

    @Test
    void afterTestLeapYearChange() {
        assertEquals(firstDayOf2025, ApiDateUtils.after(lastDayOf2024));
    }

    @Test
    void beforeTestYearChange() {
        assertEquals(lastDayOf2022, ApiDateUtils.before(firstDayOf2023));
    }

    @Test
    void afterTest2() {
        assertEquals(marchFourth, ApiDateUtils.after(marchSecond, 2));
    }

    @Test
    void beforeTest2() {
        assertEquals(marchSecond, ApiDateUtils.before(marchFourth, 2));
    }
}
