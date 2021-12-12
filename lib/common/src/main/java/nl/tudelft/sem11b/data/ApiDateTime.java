package nl.tudelft.sem11b.data;

import java.io.IOException;
import java.sql.Timestamp;
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

@Embeddable
@JsonSerialize(using = ApiDateTime.Serializer.class)
@JsonDeserialize(using = ApiDateTime.Deserializer.class)
public class ApiDateTime implements Comparable<ApiDateTime> {
    @Embedded
    private final ApiDate date;
    @Embedded
    private final ApiTime time;

    public ApiDateTime(ApiDate date, ApiTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("Neither date nor time can be null!");
        }

        this.date = date;
        this.time = time;
    }

    public ApiDateTime(long year, long month, long day, long hour, long minute) {
        this(new ApiDate(year, month, day), new ApiTime(hour, minute));
    }

    public ApiDate getDate() {
        return date;
    }

    public ApiTime getTime() {
        return time;
    }

    public String toString() {
        return date + "T" + time;
    }

    public LocalDateTime toLocal() {
        return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDay(), time.getHour(), time.getMinute());
    }

    public static ApiDateTime parse(String str) throws ParseException {
        var idx = str.indexOf('T');
        if (idx <= 0) {
            throw new ParseException("Given string is in invalid format!", 0);
        }

        return new ApiDateTime(
            ApiDate.parse(str.substring(0, idx).stripLeading()),
            ApiTime.parse(str.substring(idx + 1).stripTrailing())
        );
    }

    public static ApiDateTime from(Timestamp ts) {
        var local = ts.toLocalDateTime();
        return new ApiDateTime(
            local.getYear(), local.getMonth().getValue(), local.getDayOfMonth(),
            local.getHour(), local.getMinute()
        );
    }

    @Override
    public int compareTo(ApiDateTime other) {
        var cmp = date.compareTo(other.date);
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

    public static class Serializer extends JsonSerializer<ApiDateTime> {
        @Override
        public void serialize(ApiDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
            gen.writeString(value.toString());
        }
    }

    public static class Deserializer extends JsonDeserializer<ApiDateTime> {
        @Override
        public ApiDateTime deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
            var value = p.getValueAsString();

            if (value == null) {
                throw new ApiDateTimeDeserializeException("Date and time requires a string JSON value!",
                    p.currentLocation());
            }

            try {
                return ApiDateTime.parse(value);
            } catch (ParseException ex) {
                throw new ApiDateTimeDeserializeException("Unable to parse date or time!",
                    p.currentLocation(), ex);
            }
        }

        private static class ApiDateTimeDeserializeException extends JsonProcessingException {
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
