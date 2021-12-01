package nl.tudelft.sem11b.authentication.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getNetId() {
        User user = new User("username", "employee", "abc123");
        assertEquals("username", user.getNetId());
    }

    @Test
    void getPassword() {
        User user = new User("username", "employee", "abc123");
        assertEquals("abc123", user.getPassword());
    }

    @Test
    void getRole() {
        User user = new User("username", "employee", "abc123");
        assertEquals("employee", user.getRole());
    }

    @Test
    void setRole() {
        User user = new User("username", "employee", "abc123");
        user.setRole("admin");
        assertEquals("admin", user.getRole());
    }

    @Test
    void setInvalidRole() {
        User user = new User("username", "employee", "abc123");
        assertThrows(IllegalArgumentException.class, () -> user.setRole("Random"));
        assertEquals("employee", user.getRole());
    }

    @Test
    void testEquals() {
        User user = new User("username", "employee", "abc123");
        User user1 = new User("username", "employee", "abc123");
        assertTrue(user.equals(user1));
    }

    @Test
    void testNotEquals() {
        User user = new User("username", "employee", "abc123");
        User user1 = new User("Username", "employee", "abc123");
        assertFalse(user.equals(user1));
    }
}