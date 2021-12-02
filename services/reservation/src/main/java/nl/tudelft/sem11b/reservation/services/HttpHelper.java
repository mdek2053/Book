package nl.tudelft.sem11b.reservation.services;

import nl.tudelft.sem11b.data.exception.CommunicationException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpHelper {
    public HttpHelper() {

    }

    /**
     * Makes a GET request and returns the response.
     * @param client the HttpClient used to make the request
     * @param path the path where the request should be make
     * @param token Authorization token used, if NULL the header is not included
     * @return the HttpResponse<String> object of the response
     * @throws CommunicationException for any sort of communication error with the server
     */
    public HttpResponse<String> getResponse(HttpClient client, String path, String token) throws CommunicationException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET();

        if (token != null)
            builder = builder.header("Authorization", token);

        HttpRequest request = builder.uri(URI.create(path)).build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e){
            throw new CommunicationException();
        }
    }
}
