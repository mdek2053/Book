package nl.tudelft.sem11b.authentication.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupTest {

    User user = new User("netId", "employee", "abc");
    User user1 = new User("netId1", "employee", "abc1");
    User user2 = new User("netId2", "employee", "abc2");
    User user3 = new User("netId3", "employee", "abc3");
    List<Long> members = new ArrayList<>();
    List<Long> newMembers = new ArrayList<>();
    Group group = new Group("group", user, members, 2);
    Group group1 = new Group("group", user, members, 2);
    Group group2 = new Group("group1", user1, newMembers, 3);

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
        assertEquals(user, group.getSecretary());
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
        group.setSecretary(user1);
        assertEquals(user1, group.getSecretary());
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