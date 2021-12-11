package nl.tudelft.sem11b.data.models;

import java.util.Objects;

public class UserModel {
    private String netId;
    private String role;
    private String password;

    public UserModel(String netId, String role) {
        this.netId = netId;
        this.role = role;
    }

    public UserModel(String netId, String role, String password) {
        this.netId = netId;
        this.role = role;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return netId.equals(userModel.netId) && role.equals(userModel.role) && Objects.equals(password, userModel.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(netId, role, password);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "netId='" + netId + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
