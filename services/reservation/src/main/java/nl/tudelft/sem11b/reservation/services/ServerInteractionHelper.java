package nl.tudelft.sem11b.reservation.services;

import nl.tudelft.sem11b.reservation.exception.CommunicationException;
import nl.tudelft.sem11b.reservation.exception.UnauthorizedException;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class ServerInteractionHelper {
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final String domainUserMicroservice = "http://localhost:8082";
    private static final String domainRoomMicroservice = "http://localhost:8083";
    private HttpHelper helper = new HttpHelper();

    public ServerInteractionHelper() {
    }

    public void setHelper(HttpHelper helper) {
        this.helper = helper;
    }

    /**
     * Gets the user ID for a given token.
     * @param token the token used for authorization
     * @return the user's id
     * @throws CommunicationException if there is any sort of communication error with the server
     * @throws UnauthorizedException if the token is invalid
     */
    public long getUserId(String token) throws CommunicationException, UnauthorizedException {
        String path = domainUserMicroservice + "/users/me";

        HttpResponse<String> response = helper.getResponse(client, path, token);

        String responseBody = response.body();
        if (response.statusCode() == 403) // 403 means token is invalid
            throw new UnauthorizedException("Token is invalid");

        JSONObject obj = new JSONObject(responseBody);
        return obj.getInt("id");
    }

    /**
     * Checks if a room id is valid.
     * @param room_id the room's id
     * @return TRUE if a room with that id exists, FALSE otherwise
     * @throws CommunicationException if there is any sort of communication error with the server
     */
    public boolean checkRoomExists(long room_id) throws CommunicationException {
        String path = domainRoomMicroservice + "/room/" + room_id;

        HttpResponse<String> response = helper.getResponse(client, path, null);

        return response.statusCode() != 404;
    }

}
