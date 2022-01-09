package nl.tudelft.sem11b.data.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class UserModelTest {

    UserModel model = new UserModel(1L, "username", new String[]{"Admin"});

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
        String[] newRoles = new String[]{"Emplyee"};
        model.setRoles(newRoles);
        List<String> result = model.getRoles().collect(Collectors.toList());
        List<String> expected = Arrays.asList(newRoles);
        assertEquals(expected, result);
    }
}