package nl.tudelft.sem11b.data;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Represents a specific date in time. Implementation is using the Gregorian calendar.
 */
@Embeddable
@JsonSerialize(using = ApiDate.Serializer.class)
@JsonDeserialize(using = ApiDate.Deserializer.class)
public class ApiDate implements Comparable<ApiDate> {
    private static final Pattern RGX_FORMAT =
        Pattern.compile("(\\d+)-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])");
    private static final int[] NSCHEMA = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] LSCHEMA = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    @Column(name = "year")
    private short year;
    @Column(name = "day")
    private short day;

    /**
     * Sets the year of the date. Always must be done before setting the year.
     * @param year Year component of the date.
     */
    public void setYear(long year) {
        this.year = (short) year;
    }

    /**
     * Sets the day of the date. Always must be done after setting the year.
     * @param day Day of the year (indexed from 1)
     */
    public void setDay(long day) {
        if (day < 1 || day > daysIn(year)) {
            throw new IllegalArgumentException("Day is out of range for the given year!");
        }

        this.day = (short) (day - 1);
    }

    /**
     * Instantiates the {@link ApiDate} class.
     *
     * @param year      Year component of the date
     * @param dayOfYear Day of the year (indexed from 1)
     */
    public ApiDate(long year, long dayOfYear) {
        setYear(year);
        setDay(dayOfYear);
    }

    /**
     * Instantiates the {@link ApiDate} class.
     *
     * @param year  Year component of the date
     * @param month Month component of the date
     * @param day   Day component of the date
     */
    public ApiDate(long year, long month, long day) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(
                "Month must be an integer between 1 and 12 (inclusive)!");
        }

        var schema = isLeap(year) ? LSCHEMA : NSCHEMA;
        if (day < 1 || day > schema[(int) month - 1]) {
            throw new IllegalArgumentException("Day is out of range for the given month and year!");
        }

        this.year = (short) year;
        this.day = (short) (Arrays.stream(schema).limit(month - 1).sum() + day - 1);
    }

    private ApiDate() {
        // default constructor for entity materialization
    }

    /**
     * Gets the year component of the date.
     *
     * @return Year component of the date
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the month component of the date (indexed from 1).
     *
     * @return Month component of the date
     */
    public int getMonth() {
        var schema = isLeap(year) ? LSCHEMA : NSCHEMA;
        var day = this.day;
        for (int i = 0; i < schema.length; i++) {
            if (day < schema[i]) {
                return i + 1;
            } else {
                day -= schema[i];
            }
        }

        throw new RuntimeException("Unreachable code reached!");
    }

    /**
     * Gets the day component of the date (indexed from 1).
     *
     * @return Day component of the date
     */
    public int getDay() {
        var schema = isLeap(year) ? LSCHEMA : NSCHEMA;
        var day = this.day;
        for (int i : schema) {
            if (day < i) {
                return day + 1;
            } else {
                day -= i;
            }
        }

        throw new RuntimeException("Unreachable code reached!");
    }

    /**
     * Gets the day number of the year (indexed from 1).
     *
     * @return Day of the year
     */
    public int getDayOfYear() {
        return day + 1;
    }

    /**
     * Combines this date with a given time of day to create a time instant object.
     *
     * @param time Time of day
     * @return Combined date and time object
     */
    public ApiDateTime at(ApiTime time) {
        return new ApiDateTime(this, time);
    }

    /**
     * Combines this date with a given time of day to create a time instant object.
     *
     * @param hour   Hour of the day
     * @param minute Minute of the day
     * @return Combined date and time object
     */
    public ApiDateTime at(int hour, int minute) {
        return new ApiDateTime(this, new ApiTime(hour, minute));
    }

    /**
     * Steps a single day back in time.
     *
     * @return The day before
     */
    public ApiDate before() {
        if (day > 0) {
            // day is stored as zero-indexed, but passed as one-indexed. This has the result of
            // subtracting one from the day
            return new ApiDate(year, day);
        }

        return new ApiDate(year - 1, daysIn(year - 1));
    }

    /**
     * Steps a given number of days back in time.
     *
     * @param days Number of days to backtrack. Negative values are equivalent to zero days
     * @return Date of a day in the past
     */
    public ApiDate before(int days) {
        // TODO: Better algorithm
        var day = this;
        while (days > 0) {
            days--;
            day = day.before();
        }

        return day;
    }

    /**
     * Steps a single day forward in time.
     *
     * @return The day after
     */
    public ApiDate after() {
        if (day >= daysIn(year) - 1) {
            return new ApiDate(year + 1, 1);
        }

        // we need +2 to account for the conversion from zero-based indexing to one-based indexing
        return new ApiDate(year, day + 2);
    }

    /**
     * Steps a given number of days forward in time.
     *
     * @param days Number of days to step forward. Negative values are equivalent to zero days
     * @return Date of a day in the future
     */
    public ApiDate after(int days) {
        // TODO: Better algorithm
        var day = this;
        while (days > 0) {
            days--;
            day = day.after();
        }

        return day;
    }

    @Override
    public int compareTo(ApiDate other) {
        if (year != other.year) {
            return Short.compare(year, other.year);
        }
        return Short.compare(day, other.day);
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", getYear(), getMonth(), getDay());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return compareTo((ApiDate) o) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, day);
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
     * Gets the date before today. This is strictly equivalent to {@code ApiDate.today().before()}.
     *
     * @return Yesterday's date
     */
    public static ApiDate yesterday() {
        return today().before();
    }

    /**
     * Gets the date after today. This is strictly equivalent to {@code ApiDate.today().after()}.
     *
     * @return Tomorrow's date
     */
    public static ApiDate tomorrow() {
        return today().after();
    }

    /**
     * Checks whether a year is a leap year according to the Gregorian calendar.
     *
     * @param year Year to check
     * @return true if the year is a leap year; false otherwise
     */
    private static boolean isLeap(long year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }

    /**
     * Gets the number of days in the given year.
     *
     * @param year Year to check
     * @return Number of days in the given year
     */
    private static int daysIn(long year) {
        return isLeap(year) ? Arrays.stream(LSCHEMA).sum() : Arrays.stream(NSCHEMA).sum();
    }

    /**
     * JSON serializer for {@link ApiDate}.
     */
    public static class Serializer extends JsonSerializer<ApiDate> {
        @Override
        public void serialize(ApiDate value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
            gen.writeString(value.toString());
        }
    }

    /**
     * JSON deserializer for {@link ApiDate}.
     */
    public static class Deserializer extends JsonDeserializer<ApiDate> {
        @Override
        public ApiDate deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
            var value = p.getValueAsString();
            if (value == null) {
                throw new ApiDateDeserializeException("Date requires a string JSON value!",
                    p.currentLocation());
            }

            try {
                return ApiDate.parse(value);
            } catch (ParseException ex) {
                throw new ApiDateDeserializeException("Unable to parse a date!",
                    p.currentLocation(), ex);
            }
        }

        private static class ApiDateDeserializeException extends JsonProcessingException {
            private static final long serialVersionUID = 1L;

            protected ApiDateDeserializeException(String msg, JsonLocation loc) {
                super(msg, loc);
            }

            protected ApiDateDeserializeException(String msg, JsonLocation loc,
                                                  Throwable rootCause) {
                super(msg, loc, rootCause);
            }
        }
    }
}
