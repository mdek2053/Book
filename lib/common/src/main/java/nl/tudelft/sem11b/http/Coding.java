package nl.tudelft.sem11b.http;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Interface for data serialization/deserialization systems. Note that the interface uses
 * {@link TypeReference} instead of {@link Class}. This is because the former preserves type
 * parameter information, allowing the serialization/deserialization of generic classes.
 */
public interface Coding {
    /**
     * The MIME type of the coding system.
     *
     * @return MIME type
     */
    String getType();

    /**
     * Encodes the given data into the given writer. Note that writer should NOT be closed.
     *
     * @param writer Writer to encode the data into
     * @param value  Value to encode
     * @param type   The generics-preserving class mirror type
     * @param <T>    The type of the data being encoded
     * @throws IOException Thrown when data fails to be written into the provided writer
     */
    <T> void encode(Writer writer, T value, TypeReference<T> type) throws IOException;

    /**
     * Decodes the given type from the given reader. Note that the reader should NOT be closed.
     *
     * @param reader Reader to decode the data from
     * @param type   The generics-preserving class mirror type
     * @param <T>    The type of the data being decoded
     * @return The decoded data
     * @throws IOException Thrown when data fails to be read from the provided reader
     * @throws DecodeException Thrown when data cannot be successfully decoded
     */
    <T> T decode(Reader reader, TypeReference<T> type) throws IOException, DecodeException;
}
