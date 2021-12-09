package nl.tudelft.sem11b.http;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.exceptions.ApiException;

public class ApiClient<I extends Identity> {
    private final I identity;
    private final URI uri;
    private final Coding coding;
    private final HttpClient http;
    private final String service;

    public ApiClient(URI uri, I identity, String service) {
        this.uri = uri;
        this.identity = identity;
        this.coding = new JsonCoding();
        this.http = HttpClient.newHttpClient();
        this.service = service;
    }

    public I getIdentity() {
        return identity;
    }

    public <S> ApiResponse<S> get(String path, TypeReference<S> type) {
        return request(ApiRequest.get(getUri(path)), type);
    }

    public <Q> ApiResponse<Void> post(String path, Q body, TypeReference<Q> qType) {
        return post(path, body, qType, new TypeReference<>() {});
    }

    public <S, Q> ApiResponse<S> post(String path, Q body, TypeReference<Q> qType, TypeReference<S> sType) {
        return request(ApiRequest.post(getUri(path), body, qType), sType);
    }

    public ApiResponse<Void> delete(String path) {
        return request(ApiRequest.delete(getUri(path)), new TypeReference<>() {});
    }

    private <S> ApiResponse<S> request(ApiRequest req, TypeReference<S> type) {
        ApiResponse<S> res;
        try {
            res = http.send(req.toJava(coding), (info) -> {
                var status = info.statusCode();
                if (status == 204 || status == 404) {
                    return HttpResponse.BodySubscribers.mapping(
                        HttpResponse.BodySubscribers.discarding(),
                        i -> new ApiResponse<S>(service));
                }

                if (status / 100 == 2) {
                    return HttpResponse.BodySubscribers.mapping(
                        HttpResponse.BodySubscribers.ofInputStream(), stream -> {
                            var reader = new InputStreamReader(stream);
                            try {
                                return new ApiResponse<S>(service, coding.decode(reader, type));
                            } catch (IOException | DecodeException ex) {
                                return new ApiResponse<S>(service, new ApiException(service, ex));
                            }
                        });
                }

                return HttpResponse.BodySubscribers.mapping(
                    HttpResponse.BodySubscribers.discarding(),
                    i -> new ApiResponse<S>(service, new ApiException(service,
                        "Server responded with an unsuccessful status code! (status: "
                            + info.statusCode() + ")")));
            }).body();
        } catch (IOException ex) {
            return new ApiResponse<>(service, new ApiException(service, ex));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    private URI getUri(String path) {
        return uri.resolve(path);
    }
}
