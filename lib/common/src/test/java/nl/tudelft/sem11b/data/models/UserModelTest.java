package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nl.tudelft.sem11b.data.Roles;
import org.junit.jupiter.api.Test;

class UserModelTest {

    UserModel model = new UserModel(1L, "username", new String[]{"Admin", "Employee"});
    UserModel model2 = new UserModel(1L, "username", new String[]{"Admin", "Employee"});

    @Test
    void setId() {
        model.setId(2L);
        assertEquals(2L, model.getId());
    }

    @Test
    void setLogin() {
        model.setLogin("new_username");
        assertEquals("new_username", model.getLogin());
    }

    @Test
    void setRoles() {
        String[] newRoles = new String[]{"Employee"};
        model.setRoles(newRoles);
        List<String> result = model.getRoles().collect(Collectors.toList());
        List<String> expected = Arrays.asList(newRoles);
        assertEquals(expected, result);
    }

    @Test
    void inRoleTestFalse() {
        assertFalse(model.inRole(Roles.Secretary));
    }

    @Test
    void inRoleTestTrue() {
        assertTrue(model.inRole(Roles.Admin));
    }

    @Test
    void equalsTestSameObject() {
        assertEquals(model, model);
    }

    @Test
    void equalsTestNull() {
        assertNotEquals(model, null);
    }

    @Test
    void equalsTestDifferentClass() {
        assertNotEquals(model, " ");
    }

    @Test
    void equalsTestDifferentId() {
        model2.setId(2L);
        assertNotEquals(model, model2);
    }

    @Test
    void equalsTestDifferentLogin() {
        model2.setLogin("yeet");
        assertNotEquals(model, model2);
    }

    @Test
    void equalsTestDifferentRoles() {
        model2.setRoles(new String[]{"Employee"});
        assertNotEquals(model, model2);
    }

    @Test
    void equalsTestSuccessful() {
        assertEquals(model, model2);
    }

}