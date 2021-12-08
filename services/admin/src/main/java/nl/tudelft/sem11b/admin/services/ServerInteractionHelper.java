package nl.tudelft.sem11b.admin.services;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServerInteractionHelper {
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final String domainUserMicroservice = "http://localhost:8082";
    private HttpHelper helper = new HttpHelper();

    public ServerInteractionHelper() {
    }

    public void setHelper(HttpHelper helper) {
        this.helper = helper;
    }

    /**
     * Checks if the user with the given token has the admin role.
     * @param token user's token
     * @throws CommunicationException if there is anything wrong with the server
     * @throws UnauthorizedException if the token is invalid or the user is not an admin
     */
    public void verifyUserAdmin(String token)
            throws CommunicationException, UnauthorizedException {
        String path = domainUserMicroservice + "/users/me";

        HttpResponse<String> response = helper.getResponse(client, path, token);

        String responseBody = response.body();

        if (response.statusCode() == 403) { // 403 means token is invalid
            throw new UnauthorizedException("Token is invalid");
        }

        JSONObject obj = new JSONObject(responseBody);
        if (!obj.has("roles")) {
            throw new UnauthorizedException("User does not have the admin role");
        }

        JSONArray roles = obj.getJSONArray("roles");
        if (roles.length() == 0 || !roles.getString(0).equals("admin")) {
            throw new UnauthorizedException("User does not have the admin role");
        }

        obj.getInt("id");
    }
}
