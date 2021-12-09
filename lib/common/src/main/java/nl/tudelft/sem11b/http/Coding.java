package nl.tudelft.sem11b.http;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.fasterxml.jackson.core.type.TypeReference;

public interface Coding {
    String getType();

    <T> void encode(Writer writer, T value, TypeReference<T> type) throws IOException;

    <T> T decode(Reader reader, TypeReference<T> type) throws IOException, DecodeException;
}
