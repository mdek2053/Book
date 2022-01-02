package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserRequestModelTest {

    UserRequestModel model = new UserRequestModel("username", "password", "Admin");

    @Test
    void setLogin() {
        model.setLogin("new_username");
        assertEquals("new_username", model.getLogin());
    }

    @Test
    void setPassword() {
        model.setPassword("new_password");
        assertEquals("new_password", model.getPassword());
    }

    @Test
    void setRole() {
        model.setRole("Employee");
        assertEquals("Employee", model.getRole());
    }
}