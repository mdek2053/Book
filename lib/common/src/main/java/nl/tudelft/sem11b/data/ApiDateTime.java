package nl.tudelft.sem11b.data;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

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
 * Represents a point in time, with a minute resolution.
 */
@Embeddable
@JsonSerialize(using = ApiDateTime.Serializer.class)
@JsonDeserialize(using = ApiDateTime.Deserializer.class)
public class ApiDateTime implements Comparable<ApiDateTime> {
    @Embedded
    private final ApiDate date;
    @Embedded
    private final ApiTime time;

    /**
     * Instantiates the {@link ApiDateTime} class.
     *
     * @param date Date component of the instant in time
     * @param time Time component of the instant in time
     */
    public ApiDateTime(ApiDate date, ApiTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("Neither date nor time can be null!");
        }

        this.date = date;
        this.time = time;
    }

    /**
     * Gets the date component of the instant in time.
     *
     * @return Date
     */
    public ApiDate getDate() {
        return date;
    }

    /**
     * Gets the time component of the instant in time.
     *
     * @return Time relative to a day
     */
    public ApiTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return date + "T" + time;
    }

    /**
     * Coverts the instant into a Java time type.
     *
     * @return Local date & time object
     */
    public LocalDateTime toLocal() {
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDay(), time.getHour(),
            time.getMinute());
    }

    @Override
    public int compareTo(ApiDateTime other) {
        var cmp = ApiDateUtils.compare(date, other.date);
        if (cmp != 0) {
            return cmp;
        }

        return time.compareTo(other.time);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        return compareTo((ApiDateTime) other) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time);
    }

    public Timestamp toTimestamp() {
        return Timestamp.valueOf(this.toLocal());
    }

    /**
     * JSON serializer for {@link ApiDateTime}.
     */
    public static class Serializer extends JsonSerializer<ApiDateTime> {
        @Override
        public void serialize(ApiDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
            gen.writeString(value.toString());
        }
    }

    /**
     * JSON deserializer for {@link ApiDateTime}.
     */
    public static class Deserializer extends JsonDeserializer<ApiDateTime> {
        @Override
        public ApiDateTime deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
            var value = p.getValueAsString();

            if (value == null) {
                throw new ApiDateTimeDeserializeException(
                    "Date and time requires a string JSON value!",
                    p.currentLocation());
            }

            try {
                return ApiDateTimeUtils.parse(value);
            } catch (ParseException ex) {
                throw new ApiDateTimeDeserializeException("Unable to parse date or time!",
                    p.currentLocation(), ex);
            }
        }

        private static class ApiDateTimeDeserializeException extends JsonProcessingException {
            private static final long serialVersionUID = 1L;

            protected ApiDateTimeDeserializeException(String msg, JsonLocation loc) {
                super(msg, loc);
            }

            protected ApiDateTimeDeserializeException(String msg, JsonLocation loc,
                                                      Throwable rootCause) {
                super(msg, loc, rootCause);
            }
        }
    }
}
