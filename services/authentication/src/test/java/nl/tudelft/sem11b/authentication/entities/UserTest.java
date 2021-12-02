package nl.tudelft.sem11b.authentication.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserTest {

    private User user1 = new User("username", "employee", "abc123");
    private User user2 = new User("Username", "employee", "abc123");

    @Test
    void getNetId() {
        assertEquals("username", user1.getNetId());
    }

    @Test
    void getPassword() {
        assertEquals("abc123", user1.getPassword());
    }

    @Test
    void getRole() {
        assertEquals("employee", user1.getRole());
    }

    @Test
    void setRole() {
        user1.setRole("admin");
        assertEquals("admin", user1.getRole());
    }

    @Test
    void setInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> user1.setRole("Random"));
        assertEquals("employee", user1.getRole());
    }

    @Test
    void testEquals() {
        User user3 = new User("username", "employee", "abc123");
        assertTrue(user1.equals(user3));
    }

    @Test
    void testNotEquals() {
        assertFalse(user1.equals(user2));
    }
}