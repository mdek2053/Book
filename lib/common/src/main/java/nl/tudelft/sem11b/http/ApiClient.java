package nl.tudelft.sem11b.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.exceptions.ApiException;

/**
 * Represents a generic API client. Note that identity state of the client is represented at type
 * system level. This is on purpose, so that service proxy implementations can ensure at
 * compile-time that an authenticated API client is provided.
 *
 * @param <I> Identity state of the API client
 */
public class ApiClient<I extends Identity> {
    private final I identity;
    private final URI uri;
    private final Coding coding;
    private final HttpClient http;
    private final String service;

    /**
     * Instantiates the {@link ApiClient} class.
     *
     * @param uri      Base URI of the API
     * @param identity Identity of the API user
     * @param service  Name of the remote service
     */
    public ApiClient(URI uri, I identity, String service) {
        this.uri = uri;
        this.identity = identity;
        this.coding = new JsonCoding();
        this.http = HttpClient.newHttpClient();
        this.service = service;
    }

    /**
     * Gets the identity of the current user.
     *
     * @return Current user's identity
     */
    public I getIdentity() {
        return identity;
    }

    /**
     * Executes a {@code GET} request.
     *
     * @param path  Path and query of the request
     * @param stype The generics-preserving class mirror type of response data
     * @param <S>   Type of response data
     * @return API response object
     */
    public <S> ApiResponse<S> get(String path, TypeReference<S> stype) {
        return request(ApiRequest.get(getUri(path)), stype);
    }

    /**
     * Executes a {@code POST} request without response body.
     *
     * @param path  Path and query of the request
     * @param body  Request data
     * @param qtype The generics-preserving class mirror type of request data
     * @param <Q>   Type of request data
     * @return API response object
     */
    public <Q> ApiResponse<Void> post(String path, Q body, TypeReference<Q> qtype) {
        return post(path, body, qtype, new TypeReference<>() {});
    }

    /**
     * Executes a {@code POST} request with response body.
     *
     * @param path  Path and query of the request
     * @param body  Request data
     * @param qtype The generics-preserving class mirror type of request data
     * @param stype The generics-preserving class mirror type of response data
     * @param <S>   Type of response data
     * @param <Q>   Type of request data
     * @return API response object
     */
    public <S, Q> ApiResponse<S> post(String path, Q body, TypeReference<Q> qtype,
                                      TypeReference<S> stype) {
        return request(ApiRequest.post(getUri(path), body, qtype), stype);
    }

    /**
     * Executes a {@code DELETE} request.
     *
     * @param path Path and query of the request
     * @return API response object
     */
    public ApiResponse<Void> delete(String path) {
        return request(ApiRequest.delete(getUri(path)), new TypeReference<>() {});
    }

    /**
     * Executes a request and constructs an API response object.
     *
     * @param req  Request to execute
     * @param type The generics-preserving class mirror type of response data
     * @param <S>  Type of response data
     * @return API response object
     */
    private <S> ApiResponse<S> request(ApiRequest req, TypeReference<S> type) {
        // add authentication details to the request
        identity.authenticate(req);
        req.header("Accept", coding.getType());

        ApiResponse<S> res;
        try {
            // convert the API request to a Java HTTP request and execute it
            res = http.send(req.toJava(coding), (info) -> {
                // check if the response is supposed to be without body
                var status = info.statusCode();
                if (status == 204 || status == 404) {
                    return HttpResponse.BodySubscribers.mapping(
                        HttpResponse.BodySubscribers.discarding(),
                        i -> new ApiResponse<S>(service));
                }

                // check if response is successful
                if (status / 100 == 2) {
                    // check if response body can be deserialized with the current coding
                    var bodyType = info.headers().firstValue("Content-Type")
                        .map(ApiClient::getContentType)
                        .filter(i -> i.equalsIgnoreCase(coding.getType()));
                    if (bodyType.isEmpty()) {
                        return HttpResponse.BodySubscribers.mapping(
                            HttpResponse.BodySubscribers.discarding(),
                            i -> new ApiResponse<S>(service, new ApiException(service,
                                "Server responded with an invalid `Content-Type` header!")));
                    }

                    // decode body and wrap it into an API response
                    return HttpResponse.BodySubscribers.mapping(
                        HttpResponse.BodySubscribers.ofByteArray(), bytes -> {
                            // TODO: I would use .ofInputStream(), but Java does not expect
                            //       blocking operations here. This is potentially dangerous
                            var reader = new InputStreamReader(new ByteArrayInputStream(bytes));
                            try {
                                return new ApiResponse<S>(service, coding.decode(reader, type));
                            } catch (IOException | DecodeException ex) {
                                return new ApiResponse<S>(service, new ApiException(service, ex));
                            }
                        });
                }

                // response status code is not 2xx nor 404 (aka. it is either unexpected or
                // unsuccessful)
                return HttpResponse.BodySubscribers.mapping(
                    HttpResponse.BodySubscribers.discarding(),
                    i -> new ApiResponse<S>(service, new ApiException(service,
                        "Server responded with an unsuccessful status code! (status: "
                            + info.statusCode() + ")")));
            }).body();
        } catch (IOException ex) {
            // there was an IO problem during the request
            return new ApiResponse<>(service, new ApiException(service, ex));
        } catch (InterruptedException e) {
            // blocking operation was forcefully cancelled
            throw new RuntimeException(e);
        }

        return res;
    }

    private URI getUri(String path) {
        return uri.resolve(path);
    }

    private static String getContentType(String str) {
        if (str.contains(";")) {
            var arr = str.split(";", 2);
            return arr[0].trim();
        }

        return str.trim();
    }
}
