package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.exceptions.CommunicationException;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpHelper {
    public HttpHelper() {

    }

    public HttpResponse<String> getResponse(HttpClient client, String path, String token) throws CommunicationException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Authorization", token)
                .uri(URI.create(path))
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e){
            throw new CommunicationException();
        }
    }
}
