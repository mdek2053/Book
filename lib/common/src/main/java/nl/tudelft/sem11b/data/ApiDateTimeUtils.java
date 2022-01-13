package nl.tudelft.sem11b.data;

import java.sql.Timestamp;
import java.text.ParseException;

public class ApiDateTimeUtils {
    /**
     * Attempts to parse the date and time from its string representation. Note that surrounding
     * whitespace and leading zeros are permitted.
     *
     * @param str String to parse
     * @return Parsed instant
     * @throws ParseException When string is in invalid format or the component values are outside
     *                        their respective bounds
     */
    public static ApiDateTime parse(String str) throws ParseException {
        var idx = str.indexOf('T');
        if (idx <= 0) {
            throw new ParseException("Given string is in invalid format!", 0);
        }

        return new ApiDateTime(
                ApiDateUtils.parse(str.substring(0, idx).stripLeading()),
                ApiTime.parse(str.substring(idx + 1).stripTrailing())
        );
    }

    /**
     * Converts a Java time type into an API time type. Note that all time resolution beyond minutes
     * is lost.
     *
     * @param ts Java timestamp
     * @return API date and time
     */
    public static ApiDateTime from(Timestamp ts) {
        var local = ts.toLocalDateTime();
        return new ApiDateTime(
                new ApiDate(local.getYear(), local.getMonth().getValue(), local.getDayOfMonth()),
                new ApiTime(local.getHour(), local.getMinute())
        );
    }

}
