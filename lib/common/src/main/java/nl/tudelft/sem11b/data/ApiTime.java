package nl.tudelft.sem11b.data;

import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;

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
 * Represents a point in time relative to a single day, with a minute resolution.
 */
@Embeddable
@JsonSerialize(using = ApiTime.Serializer.class)
@JsonDeserialize(using = ApiTime.Deserializer.class)
public class ApiTime implements Comparable<ApiTime> {
    /**
     * Minimal value. Equivalent to midnight.
     */
    public static final ApiTime MINIMUM = new ApiTime(0, 0);
    /**
     * Maximal value. Equivalent to a single minute before midnight.
     */
    public static final ApiTime MAXIMUM = new ApiTime(23, 59);
    private static final Pattern RGX_FORMAT =
        Pattern.compile("(0?[0-9]|1[0-9]|2[0-3]):(0?[0-9]|[1-5][0-9])");

    @Column(name = "timestamp")
    private short timestamp;

    /**
     * Instantiates the {@link ApiTime} class.
     *
     * @param hour   Hour of the day
     * @param minute Minute of the hour
     */
    public ApiTime(long hour, long minute) {
        if (hour < 0 || hour >= 24) {
            throw new IllegalArgumentException("Hour must be a non-negative integer less than 24!");
        }

        if (minute < 0 || minute >= 60) {
            throw new IllegalArgumentException(
                "Minute must be a non-negative integer less than 60!");
        }

        this.timestamp = (short) (minute + 60 * hour);
    }

    private ApiTime() {
        // default constructor for entity materialization
    }

    /**
     * Gets the hour of the day.
     *
     * @return Hour of the day
     */
    public int getHour() {
        return timestamp / 60;
    }

    /**
     * Gets the minute of the hour.
     *
     * @return Minute of the hour
     */
    public int getMinute() {
        return timestamp % 60;
    }

    @Override
    public int compareTo(ApiTime other) {
        return Short.compare(timestamp, other.timestamp);
    }

    @Override public String toString() {
        return String.format("%02d:%02d", getHour(), getMinute());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return compareTo((ApiTime) o) == 0;
    }

    @Override
    public int hashCode() {
        return timestamp;
    }

    /**
     * Attempts to parse the time of day from its string representation. Note that surrounding
     * whitespace and leading zeros are permitted.
     *
     * @param str String to parse
     * @return Parsed time of day
     * @throws ParseException When string is in invalid format or the component values are outside
     *                        their respective bounds
     */
    public static ApiTime parse(String str) throws ParseException {
        var matches = RGX_FORMAT.matcher(str.trim());
        if (!matches.matches()) {
            throw new ParseException("Given string is in invalid format!", 0);
        }

        var hour = matches.group(1);
        var minute = matches.group(2);

        return new ApiTime(Byte.parseByte(hour), Byte.parseByte(minute));
    }

    /**
     * JSON serializer for {@link ApiTime}.
     */
    public static class Serializer extends JsonSerializer<ApiTime> {
        @Override
        public void serialize(ApiTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
            gen.writeString(value.toString());
        }
    }

    /**
     * JSON deserializer for {@link ApiTime}.
     */
    public static class Deserializer extends JsonDeserializer<ApiTime> {

        @Override
        public ApiTime deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
            var value = p.getValueAsString();
            if (value == null) {
                throw new TimeOfDayDeserializeException("Time of day requires a string JSON value!",
                    p.currentLocation());
            }

            try {
                return ApiTime.parse(value);
            } catch (ParseException ex) {
                throw new TimeOfDayDeserializeException("Unable to parse time of day!",
                    p.currentLocation(), ex);
            }
        }

        private static class TimeOfDayDeserializeException extends JsonProcessingException {
            protected TimeOfDayDeserializeException(String msg, JsonLocation loc) {
                super(msg, loc);
            }

            protected TimeOfDayDeserializeException(String msg, JsonLocation loc,
                                                    Throwable rootCause) {
                super(msg, loc, rootCause);
            }
        }
    }
}
