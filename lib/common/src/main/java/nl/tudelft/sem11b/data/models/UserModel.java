package nl.tudelft.sem11b.data.models;

import java.util.Arrays;
import java.util.stream.Stream;

import nl.tudelft.sem11b.data.Roles;

/**
 * Holds all information about a user.
 */
public class UserModel {
    private transient long id;
    private transient String login;
    private transient String[] roles;

    /**
     * Instantiates the {@link UserModel} class.
     *
     * @param id    Unique numeric identifier of the user
     * @param login NetID of the user
     * @param roles Roles the user has
     */
    public UserModel(long id, String login, String[] roles) {
        this.id = id;
        this.login = login;
        this.roles = roles;
    }

    private UserModel() {
        // default constructor for model materialization
    }

    /**
     * Gets the unique numeric identifier of the user.
     *
     * @return ID of the user
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the NetID of the user.
     *
     * @return NetID of the user
     */
    public String getLogin() {
        return login;
    }

    /**
     * Gets all the roles the user is in (as strings).
     *
     * @return Roles of user
     */
    public Stream<String> getRoles() {
        return roles != null ? Arrays.stream(roles) : Stream.empty();
    }

    /**
     * Checks if the user has the given role.
     *
     * @param role Role to check
     * @return {@code true} if the user has the given role; {@code false} otherwise
     */
    public boolean inRole(Roles role) {
        return getRoles().anyMatch(i -> i.equalsIgnoreCase(role.toString()));
    }
}
