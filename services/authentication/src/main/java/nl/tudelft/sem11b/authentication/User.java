package nl.tudelft.sem11b.authentication;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;


public class User {

    private String netId;
    private String role;
    private int groupNr;

    public User(String netId, String role, int groupNr) {
        this.netId = netId;
        this.role = role;
        this.groupNr = groupNr;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getGroupNr() {
        return groupNr;
    }

    public void setGroupNr(int groupNr) {
        this.groupNr = groupNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getGroupNr() == user.getGroupNr() &&
                getNetId().equals(user.getNetId()) &&
                getRole().equals(user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNetId(), getRole(), getGroupNr());
    }
}
