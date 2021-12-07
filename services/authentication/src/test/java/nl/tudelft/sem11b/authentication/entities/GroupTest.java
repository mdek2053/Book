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
    List<User> members = new ArrayList<>();
    List<User> newMembers = new ArrayList<>();
    Group group = new Group(user, members, 2);
    Group group1 = new Group(user, members, 2);
    Group group2 = new Group(user1, newMembers, 3);

    @BeforeEach
    void setup() {
        members.add(user1);
        members.add(user2);
        newMembers.add(user3);
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
    void setSecretary() {
        group.setSecretary(user1);
        assertEquals(user1, group.getSecretary());
    }

    @Test
    void setGroupMembers() {
        List<User> newMembers = new ArrayList<>();
        newMembers.add(user1);
        group.setGroupMembers(newMembers);
        assertEquals(newMembers, group.getGroupMembers());
    }

    @Test
    void addToGroupMembers() {

        List<User> updated = members;
        updated.add(user3);
        assertEquals(updated, group.addToGroupMembers(newMembers));
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