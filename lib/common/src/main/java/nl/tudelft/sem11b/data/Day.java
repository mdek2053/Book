package nl.tudelft.sem11b.data;

import java.io.IOException;
import java.text.ParseException;
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

@Embeddable
@JsonSerialize(using = Day.Serializer.class)
@JsonDeserialize(using = Day.Deserializer.class)
public class Day implements Comparable<Day> {
    private static final Pattern RGX_FORMAT =
        Pattern.compile("(\\d+)-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])");
    private static final int[] NSCHEMA = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] LSCHEMA = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    @Column(name = "year")
    private short year;
    @Column(name = "day")
    private short day;

    public Day(long year, long dayOfYear) {
        var leap = isLeap(year);
        if (dayOfYear < 1 || (!leap && dayOfYear >= 365) || (leap && dayOfYear >= 366)) {
            throw new IllegalArgumentException("Day is out of range for the given year!");
        }

        this.year = (short) year;
        this.day = (short) (dayOfYear - 1);
    }

    public Day(long year, long month, long day) {
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

    private Day() {
        // default constructor for entity materialization
    }

    public int getYear() {
        return year;
    }

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

    public int getDayOfYear() {
        return day + 1;
    }

    @Override
    public int compareTo(Day other) {
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

        return compareTo((Day) o) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, day);
    }

    public static Day parse(String str) throws ParseException {
        var matches = RGX_FORMAT.matcher(str.trim());
        if (!matches.matches()) {
            throw new ParseException("Given string is in invalid format!", 0);
        }

        var year = Short.parseShort(matches.group(1));
        var month = Byte.parseByte(matches.group(2));
        var day = Byte.parseByte(matches.group(3));

        try {
            return new Day(year, month, day);
        } catch (IllegalArgumentException ex) {
            throw new ParseException("Invalid date!", 0);
        }
    }

    private static boolean isLeap(long year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 == 0);
    }

    public static class Serializer extends JsonSerializer<Day> {
        @Override
        public void serialize(Day value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
            gen.writeString(value.toString());
        }
    }

    public static class Deserializer extends JsonDeserializer<Day> {
        @Override
        public Day deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JacksonException {
            var value = p.nextTextValue();
            if (value == null) {
                throw new DayDeserializeException("Date requires a string JSON value!",
                    p.currentLocation());
            }

            try {
                return Day.parse(value);
            } catch (ParseException ex) {
                throw new DayDeserializeException("Unable to parse a date!",
                    p.currentLocation(), ex);
            }
        }

        private static class DayDeserializeException extends JsonProcessingException {
            protected DayDeserializeException(String msg, JsonLocation loc) {
                super(msg, loc);
            }

            protected DayDeserializeException(String msg, JsonLocation loc,
                                              Throwable rootCause) {
                super(msg, loc, rootCause);
            }
        }
    }
}
