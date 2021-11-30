package nl.tudelft.sem11b.reservation;

import nl.tudelft.sem11b.reservation.exceptions.BadTokenException;
import nl.tudelft.sem11b.reservation.exceptions.CommunicationException;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class ServerInteractionHelper {
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final String domainUserMicroservice = "http://localhost:8082";
    private HttpHelper helper = new HttpHelper();

    public ServerInteractionHelper() {
    }

    public void setHelper(HttpHelper helper) {
        this.helper = helper;
    }

    public long getUserId(String token) throws CommunicationException, BadTokenException {
        String path = domainUserMicroservice + "/users/me";

        HttpResponse<String> response = helper.getResponse(client, path, token);

        String responseBody = response.body();
        if (response.statusCode() == 403)
            throw new BadTokenException();

        JSONObject obj = new JSONObject(responseBody);
        return obj.getInt("id");
    }

}
