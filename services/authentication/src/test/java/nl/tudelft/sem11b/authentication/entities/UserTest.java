package nl.tudelft.sem11b.authentication.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserTest {

    String role = "employee";
    private transient User user1 = new User("username1", role, "abc1");
    private transient User user2 = new User("Username2", role, "abc2");

    @Test
    void getNetId() {
        assertEquals("username1", user1.getNetId());
    }

    @Test
    void getPassword() {
        assertEquals("abc1", user1.getPassword());
    }

    @Test
    void getRole() {
        assertEquals(role, user1.getRole());
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
        User user3 = new User("username1", role, "abc1");
        assertTrue(user1.equals(user3));
    }

    @Test
    void testNotEquals() {
        assertFalse(user1.equals(user2));
    }
}