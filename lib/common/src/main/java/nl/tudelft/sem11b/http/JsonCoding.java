package nl.tudelft.sem11b.http;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JsonCoding implements Coding {
    private final ObjectMapper mapper;

    public JsonCoding() {
        this.mapper = new JsonMapper()
            .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
    }

    @Override
    public String getType() {
        return "application/json";
    }

    @Override
    public <T> void encode(Writer writer, T value, TypeReference<T> type) throws IOException {
        mapper.writeValue(writer, value);
    }

    @Override
    public <T> T decode(Reader reader, TypeReference<T> type) throws IOException, DecodeException {
        try {
            return mapper.readValue(reader, type);
        } catch (DatabindException | StreamReadException ex) {
            throw new DecodeException(ex);
        }
    }
}
