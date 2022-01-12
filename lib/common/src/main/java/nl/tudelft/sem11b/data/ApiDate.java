package nl.tudelft.sem11b.data;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

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
public class ApiDate {
    @Transient
    private final transient int[] schema = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


    @Column(name = "year")
    private Integer year;
    @Column(name = "month")
    private Integer month;
    @Column(name = "day")
    private Integer day;

    /**
     * Sets the year of the date. Always must be done before setting the year.
     * @param year Year component of the date.
     */
    public void setYear(int year) {
        this.year = year;
        if (isLeap()) {
            schema[1] = 29;
        }
    }

    /**
     * Sets the month of the date.
     * @param month month component of the date (Indexed from 1)
     */
    public void setMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month is out of range!");
        }
        this.month = month;
    }

    /**
     * Sets the day of the date. Always must be done after setting the month and year.
     * @param day Day of the year (indexed from 1)
     */
    public void setDay(int day) {
        if (day < 1 || day > schema[month - 1]) {
            throw new IllegalArgumentException("Day i out of range for given year!");
        }

        this.day = day;
    }


    /**
     * Instantiates the {@link ApiDate} class.
     *
     * @param year  Year component of the date
     * @param month Month component of the date
     * @param day   Day component of the date
     */
    public ApiDate(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDay(day);
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
        return month;
    }

    /**
     * Gets the day component of the date (indexed from 1).
     *
     * @return Day component of the date
     */
    public int getDay() {
        return day;
    }

    public int[] getSchema() {
        return schema;
    }


    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiDate other = (ApiDate) o;

        return year.equals(other.year) && month.equals(other.month) && day.equals(other.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    /**
     * Checks whether the current year is in a leap year according to the Gregorian calendar.
     *
     * @return true if the year is a leap year; false otherwise
     */
    private boolean isLeap() {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
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
                return ApiDateUtils.parse(value);
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
