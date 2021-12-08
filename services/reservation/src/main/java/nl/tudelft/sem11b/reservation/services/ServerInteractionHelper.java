package nl.tudelft.sem11b.reservation.services;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem11b.data.exception.CommunicationException;
import nl.tudelft.sem11b.data.exception.UnauthorizedException;
import nl.tudelft.sem11b.data.models.BuildingObject;
import nl.tudelft.sem11b.data.models.RoomObject;
import org.assertj.core.util.Lists;
import org.json.JSONObject;

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

        if (response.statusCode() == 403) { // 403 means token is invalid
            throw new UnauthorizedException("Token is invalid");
        }

        JSONObject obj = new JSONObject(responseBody);
        return obj.getInt("id");
    }

    /**
     * Checks if a room id is valid.
     * @param roomId the room's id
     * @return TRUE if a room with that id exists, FALSE otherwise
     * @throws CommunicationException if there is any sort of communication error with the server
     */
    public boolean checkRoomExists(long roomId) throws CommunicationException {
        String path = domainRoomMicroservice + "/rooms/" + roomId;

        HttpResponse<String> response = helper.getResponse(client, path, null);

        return response.statusCode() != 404;
    }

    /**
     * Gets the opening hours for a room.
     * @param roomId the room's id
     * @return a List with two elements: the opening hour and the closing hour
     * @throws CommunicationException if there is any sort of communication error with the server
     */
    public List<String> getOpeningHours(long roomId) throws CommunicationException {
        String path = domainRoomMicroservice + "/rooms/" + roomId;

        HttpResponse<String> response = helper.getResponse(client, path, null);
        RoomObject room;
        try {
            room = new ObjectMapper().readValue(response.body(), RoomObject.class);
        } catch (JsonProcessingException e) {
            throw new CommunicationException();
        }

        BuildingObject building = room.getBuilding();
        return Lists.list(building.getOpen(), building.getClose());
    }

    /**
     * Checks if a room is under maintenance and gets the reason.
     * @param roomId the room's id
     * @return a String with the maintenance reason if under maintenance, null otherwise
     * @throws CommunicationException if there is any sort of communication error with the server
     */
    public String getMaintenance(long roomId) throws CommunicationException {
        String path = domainRoomMicroservice + "/rooms/" + roomId;

        HttpResponse<String> response = helper.getResponse(client, path, null);
        RoomObject room;
        try {
            room = new ObjectMapper().readValue(response.body(), RoomObject.class);
        } catch (JsonProcessingException e) {
            throw new CommunicationException();
        }

        if (room.getClosure() == null) {
            return null;
        }

        return room.getClosure().getDescription();
    }

    /**
     * Gets the role of user with a given token.
     * @param userToken the token used for authorization
     * @return the user's role
     * @throws CommunicationException if there is any sort of communication error with the server
     * @throws UnauthorizedException if the token is invalid
     */
    public String getUserRole(String userToken)
            throws UnauthorizedException, CommunicationException {
        String path = domainUserMicroservice + "/users/me";

        HttpResponse<String> response = helper.getResponse(client, path, userToken);

        String responseBody = response.body();

        if (response.statusCode() == 403) { // 403 means token is invalid
            throw new UnauthorizedException("Token is invalid");
        }

        JSONObject obj = new JSONObject(responseBody);
        return obj.getString("role");
    }
}
