package nl.tudelft.sem11b.data.models;

import java.util.Arrays;
import java.util.stream.Stream;

import nl.tudelft.sem11b.data.Roles;

public class UserModel {
    private long id;
    private String login;
    private String[] roles;

    public UserModel(long id, String login, String[] roles) {
        this.id = id;
        this.login = login;
        this.roles = roles;
    }

    private UserModel() {}

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Stream<String> getRoles() {
        return roles != null ? Arrays.stream(roles) : Stream.empty();
    }

    public boolean inRole(Roles role) {
        return getRoles().anyMatch(i -> i.equalsIgnoreCase(role.toString()));
    }
}
