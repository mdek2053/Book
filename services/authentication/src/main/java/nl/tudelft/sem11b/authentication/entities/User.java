package nl.tudelft.sem11b.authentication.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * Class for generating new users and getting different attributes of a specific user.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String netId;
    private String role;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public User() {}


    /**
     * Constructor for creating new users.
     * @param netId provides the username of the user in the system.
     * @param role provides which role the user has in the system,
     *             can be a employee, secretary or admin.
     * @param password provides the password of the user which the user can log in with.
     */
    public User(String netId, String role, String password) {
        this.netId = netId;
        this.role = role;
        this.password = password;
    }

    public String getNetId() {
        return netId;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    /**
     * First checks whether the provided role is valid.
     * After that, sets the role of the user.
     * @param role contains the role of the user in the system.
     */
    public void setRole(String role) {
        if (role.equals("employee") || role.equals("secretary") || role.equals("admin")) {
            this.role = role;
        } else {
            throw new IllegalArgumentException("Invalid role specified");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return getNetId().equals(user.getNetId())
                && getRole().equals(user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNetId(), getRole());
    }
}
