package nl.tudelft.sem11b.authentication.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupTest {

    transient String role = "employee";
    transient User user = new User("netId", role, "abc");
    transient User user1 = new User("netId1", role, "abc1");
    transient User user2 = new User("netId2", role, "abc2");
    transient User user3 = new User("netId3", role, "abc3");
    transient List<Long> members = new ArrayList<>();
    transient List<Long> newMembers = new ArrayList<>();
    transient Group group = new Group("group", user.getId(), members, 2L);
    transient Group group1 = new Group("group", user.getId(), members, 2L);
    transient Group group2 = new Group("group1", user1.getId(), newMembers, 3L);

    @BeforeEach
    void setup() {
        members.add(user1.getId());
        members.add(user2.getId());
        newMembers.add(user3.getId());
        group.setGroupMembers(members);
    }

    @Test
    void getGroupId() {
        assertEquals(2, group.getGroupId());
    }

    @Test
    void getSecretary() {
        assertEquals(user.getId(), group.getSecretary());
    }

    @Test
    void getGroupMembers() {
        assertEquals(members, group.getGroupMembers());
    }

    @Test
    void getName() {
        assertEquals("group", group.getName());
    }

    @Test
    void setSecretary() {
        group.setSecretary(user1.getId());
        assertEquals(user1.getId(), group.getSecretary());
    }

    @Test
    void setGroupMembers() {
        group.setGroupMembers(newMembers);
        assertEquals(newMembers, group.getGroupMembers());
    }

    @Test
    void setName() {
        group.setName("newGroup");
        assertEquals("newGroup", group.getName());
    }

    @Test
    void testEquals() {
        assertTrue(group.equals(group1));
    }

    @Test
    void testNotEquals() {
        assertFalse(group.equals(group2));
    }
}