package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GroupModelTest {

    long id = 1L;
    long secretaryId = 6L;
    GroupModel model = new GroupModel("Team", secretaryId, new ArrayList<>(), 15L);
    GroupModel model2 = new GroupModel("Team", secretaryId, new ArrayList<>(), 15L);
    List<Long> members = new ArrayList<>();

    @BeforeEach
    void setUp() {
        members.add(1L);
        members.add(2L);
        members.add(3L);
    }

    @Test
    void testNotNull() {
        GroupModel notNull = new GroupModel();
        assertNotNull(notNull);
    }

    @Test
    void testSetGroupId() {
        model.setGroupId(id);
        assertEquals(id, model.getGroupId());
    }

    @Test
    void testSetName() {
        model.setName("Different team");
        assertEquals("Different team", model.getName());
    }

    @Test
    void testSetSecretary() {
        model.setSecretary(66L);
        assertEquals(66L, model.getSecretary());
    }

    @Test
    void testSetGroupMembers() {
        model.setGroupMembers(members);
        assertEquals(members, model.getGroupMembers());
    }

    @Test
    void testAddGroupMembers() {
        model.setGroupMembers(members);
        List<Long> addMembers = new ArrayList<>();
        addMembers.add(1L);
        addMembers.add(4L);
        model.addToGroupMembers(addMembers);
        members.add(4L);
        assertEquals(members, model.getGroupMembers());
    }

    @Test
    void testSameEqual() {
        assertEquals(model, model);
    }

    @Test
    void testNotInstance() {
        assertFalse(model.equals(new Object()));
    }

    @Test
    void testEqualsNull() {
        assertFalse(model.equals(null));
    }

    @Test
    void testEqualsDifferentId() {
        model2.setGroupId(17L);
        assertFalse(model.equals(model2));
    }

    @Test
    void testEqualsDifferentName() {
        model2.setName("Chonkers");
        assertFalse(model.equals(model2));
    }

    @Test
    void testEqualsDifferentSecretary() {
        model2.setSecretary(18L);
        assertFalse(model.equals(model2));
    }

    @Test
    void testEqualsDifferentMembers() {
        model2.setGroupMembers(null);
        assertFalse(model.equals(model2));
    }

    @Test
    void testEqualsSuccessful() {
        assertTrue(model.equals(model2));
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hash(model.getGroupId(), model.getName(),
                model.getSecretary(), model.getGroupMembers()),
                model.hashCode());
    }
}