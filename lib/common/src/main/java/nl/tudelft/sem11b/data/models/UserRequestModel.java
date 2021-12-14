package nl.tudelft.sem11b.data.models;

/**
 * Holds information used to create a new user.
 */
public class UserRequestModel {
    private String login;
    private String password;
    private String role;

    /**
     * Instantiates the {@link UserRequestModel} class.
     *
     * @param login    User's NetID
     * @param password User's password in plain-text
     * @param role     User's role
     */
    public UserRequestModel(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    private UserRequestModel() {

    }

    /**
     * Gets the user's NetID.
     *
     * @return NetID
     */
    public String getLogin() {
        return login;
    }

    /**
     * Gets the user's password in plain-text.
     *
     * @return Password in plain-text
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user's role.
     *
     * @return Role
     */
    public String getRole() {
        return role;
    }
}
