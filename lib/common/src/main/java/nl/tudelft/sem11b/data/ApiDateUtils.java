package nl.tudelft.sem11b.data;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.regex.Pattern;

public class ApiDateUtils implements Comparator<ApiDate> {
    private static final Pattern RGX_FORMAT =
            Pattern.compile("(\\d+)-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])");

    /**
     * Steps a single day back in time.
     *
     * @return The day before
     */
    public static ApiDate before(ApiDate date) {
        int day = date.getDay();
        int month = date.getMonth();
        int year = date.getYear();
        if(day > 1) {
            return new ApiDate(year, month, day - 1);
        }
        if(month > 1) {
            return new ApiDate(year, month - 1, date.getSchema()[month - 1]);
        }
        return new ApiDate(year - 1, 12, 31);
    }

    /**
     * Steps a given number of days back in time.
     *
     * @param days Number of days to backtrack. Negative values are equivalent to zero days
     * @return Date of a day in the past
     */
    public static ApiDate before(ApiDate date, int days) {
        // TODO: Better algorithm
        ApiDate day = date;
        while (days > 0) {
            days--;
            day = before(day);
        }

        return day;
    }

    /**
     * Steps a single day forward in time.
     *
     * @return The day after
     */
    public static ApiDate after(ApiDate date) {
        int day = date.getDay();
        int month = date.getMonth();
        int year = date.getYear();

        if (day == date.getSchema()[month - 1]) {
            if (month == 12) {
                return new ApiDate(year + 1, 1, 1);
            }
            return new ApiDate(year, month + 1, 1);
        }
        return new ApiDate(year, month, day + 1);
    }

    /**
     * Steps a given number of days forward in time.
     *
     * @param days Number of days to step forward. Negative values are equivalent to zero days
     * @return Date of a day in the future
     */
    public static ApiDate after(ApiDate date, int days) {
        // TODO: Better algorithm
        ApiDate day = date;
        while (days > 0) {
            days--;
            day = after(day);
        }

        return day;
    }

    @Override
    public int compare(ApiDate date1, ApiDate date2) {

        if (date1.getYear() != date2.getYear()) {
            return Integer.compare(date1.getYear(), date2.getYear());
        }
        if (date1.getMonth() != date2.getMonth()) {
            return Integer.compare(date1.getMonth(), date2.getMonth());
        }
        return Integer.compare(date1.getDay(), date2.getDay());
    }

    /**
     * Attempts to parse the date from its string representation. Note that surrounding
     * whitespace and leading zeros are permitted.
     *
     * @param str String to parse
     * @return Parsed date
     * @throws ParseException When string is in invalid format or the component values are outside
     *                        their respective bounds
     */
    public static ApiDate parse(String str) throws ParseException {
        var matches = RGX_FORMAT.matcher(str.trim());
        if (!matches.matches()) {
            throw new ParseException("Given string is in invalid format!", 0);
        }

        var year = Short.parseShort(matches.group(1));
        var month = Byte.parseByte(matches.group(2));
        var day = Byte.parseByte(matches.group(3));

        try {
            return new ApiDate(year, month, day);
        } catch (IllegalArgumentException ex) {
            throw new ParseException("Invalid date!", 0);
        }
    }

    /**
     * Gets current date.
     *
     * @return Current date
     */
    public static ApiDate today() {
        var now = LocalDate.now();
        return new ApiDate(now.getYear(), now.getMonth().getValue(), now.getDayOfMonth());
    }

    /**
     * Gets the date before today.
     *
     * @return Yesterday's date
     */
    public static ApiDate yesterday() {
        return before(today());
    }

    /**
     * Gets the date after today
     *
     * @return Tomorrow's date
     */
    public static ApiDate tomorrow() {
        return after(today());
    }


    /**
     * Returns the day at the given time.
     * @param date The day
     * @param hour The hour
     * @param minute The minute
     * @return The new ApiTimeDate
     */
    public static ApiDateTime at(ApiDate date, int hour, int minute) {
        return new ApiDateTime(date, new ApiTime(hour, minute));
    }


}
