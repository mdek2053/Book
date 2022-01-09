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
    transient User user = new User(1L, "netId", role, "abc");
    transient User user1 = new User(2L, "netId1", role, "abc1");
    transient User user2 = new User(3L, "netId2", role, "abc2");
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
        group.setGroupId(3L);
        assertEquals(3L, group.getGroupId());
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
    void testAddGroupMembers() {
        newMembers.add(user2.getId());
        group.addToGroupMembers(newMembers);
        List<Long> list = new ArrayList<>();
        list.add(user1.getId());
        list.add(user2.getId());
        list.add(user3.getId());
        Group expected = new Group("group", user.getId(), list, 2L);
        assertEquals(expected, group);
    }

    @Test
    void testEquals() {
        assertTrue(group.equals(group1));
    }

    @Test
    void testNotEquals() {
        assertFalse(group.equals(group2));
    }

    @Test
    void testNotEqualsSecretary() {
        Group diff = new Group("group", user1.getId(), members, 2L);
        assertFalse(group.equals(diff));
    }

    @Test
    void testNotEqualsMembers() {
        Group diff = new Group("group", user.getId(), new ArrayList<>(), 2L);
        assertFalse(group.equals(diff));
    }

    @Test
    void testNotEqualsGroupId() {
        Group diff = new Group("group", user.getId(), members, 3L);
        assertFalse(group.equals(diff));
    }

    @Test
    void testNotEqualsName() {
        Group diff = new Group("diff", user.getId(), members, group.getGroupId());
        assertFalse(group.equals(diff));
    }

    @Test
    void testTheSame() {
        assertEquals(group, group);
    }

    @Test
    void testNotEqualsObject() {
        assertFalse(group.equals(new Object()));
    }

    @Test
    void testNotEqualsNull() {
        assertFalse(group.equals(null));
    }
}