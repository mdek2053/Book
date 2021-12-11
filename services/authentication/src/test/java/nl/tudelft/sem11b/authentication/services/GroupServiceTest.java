package nl.tudelft.sem11b.authentication.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.authentication.repositories.GroupRepository;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import nl.tudelft.sem11b.data.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class GroupServiceTest {

    GroupRepository groupRepository = mock(GroupRepository.class);

    UserRepository userRepository = mock(UserRepository.class);

    @InjectMocks
    GroupServiceImpl groupService;

    User user1 = new User("User", "employee", "abc");
    User user2 = new User("User2", "employee", "abc1");
    User user3 = new User("User3", "employee", "abc2");
    User user4 = new User("User4", "employee", "abc3");

    UserModel userModel1 = new UserModel("User", "employee", "abc");
    UserModel userModel2 = new UserModel("User2", "employee", "abc1");
    UserModel userModel3 = new UserModel("User3", "employee", "abc2");
    UserModel userModel4 = new UserModel("User4", "employee", "abc3");

    List<Long> users1 = new ArrayList<>();
    List<Long> users2 = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
    Group group1 = new Group("group", user1, new ArrayList<>(), 2);
    Group group2 = new Group("group1", user1, new ArrayList<>(), 4);

    @BeforeEach
    void setup() {
        users1.add(user2.getId());
        users1.add(user3.getId());
        users1.add(user4.getId());
        users2.add(user3.getId());
        group1.setGroupMembers(users1);
        group2.setGroupMembers(users2);
        groups.add(group1);
        groups.add(group2);
    }

    @Test
    void getGroupsOfUserNoGroups() {
        when(groupRepository.findAllByGroupIdExists()).thenReturn(Optional.of(new ArrayList<>()));
        assertThrows(NoAssignedGroupException.class, () -> groupService.getGroupsOfUser(user1));
    }

    //@Test
    void getGroupsOfUserExistingGroups() throws NoAssignedGroupException {
        when(groupRepository.findAllByGroupIdExists()).thenReturn(Optional.of(groups));
        List<Group> result = new ArrayList<>();
        result.add(group1);
        assertEquals(result, groupService.getGroupsOfUser(user4));
    }

    @Test
    void saveGroup() {
        groupService.saveGroup(group1);
        verify(groupRepository, times(1)).save(group1);
    }

    //@Test
    void addGroupNoPreviousGroupId() throws InvalidGroupCredentialsException,
            InvalidCredentialsException {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user1));
        Group group = new Group("group", user1, users1, 1);
        assertEquals(group, groupService.addGroup("group", user1, users1));
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void verifyInvalidUsers() {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());
        assertThrows(InvalidGroupCredentialsException.class, () -> groupService
                .verifyUsers(users1));
    }
}