package nl.tudelft.sem11b.authentication.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.tudelft.sem11b.data.models.UserModel;


/**
 * Class for generating new users and getting different attributes of a specific user.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "netId", nullable = false)
    private String netId;

    @Column(name = "role", nullable = false)
    private String role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    public User() {

    }

    /**
     * Constructor for testing purposes of creating users.
     *
     * @param id       provides the id of the user.
     * @param netId    provides the username of the user in the system.
     * @param role     provides which role the user has in the system,
     *                 can be a employee, secretary or admin.
     * @param password provides the password of the user which the user can log in with.
     */
    public User(Long id, String netId, String role, String password) {
        this.id = id;
        this.netId = netId;
        this.role = role;
        this.password = password;
    }

    /**
     * Constructor for creating new users.
     *
     * @param netId    provides the username of the user in the system.
     * @param role     provides which role the user has in the system,
     *                 can be a employee, secretary or admin.
     * @param password provides the password of the user which the user can log in with.
     */
    public User(String netId, String role, String password) {
        this.netId = netId;
        this.role = role;
        this.password = password;
    }

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * First checks whether the provided role is valid.
     * After that, sets the role of the user.
     *
     * @param role contains the role of the user in the system.
     */
    public void setRole(String role) {
        if (role.equals("employee") || role.equals("admin")) {
            this.role = role;
        } else {
            throw new IllegalArgumentException("Invalid role specified");
        }
    }

    /**
     * Converts this entity into it's equivalent model.
     *
     * @return Model of this entity
     */
    public UserModel toModel() {
        String[] roles = new String[]{role};
        return new UserModel(id, netId, roles);
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
