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
import nl.tudelft.sem11b.authentication.repositories.GroupRepository;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class GroupServiceTest {

    GroupRepository groupRepository = mock(GroupRepository.class);

    UserRepository userRepository = mock(UserRepository.class);

    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @InjectMocks
    GroupServiceImpl groupService;

    User user1 = new User(1L, "User", "employee", "abc");
    User user2 = new User(2L, "User2", "employee", "abc1");
    User user3 = new User(3L, "User3", "employee", "abc2");
    User user4 = new User(4L, "User4", "employee", "abc3");

    UserModel userModel1 = new UserModel(1L, "User", "employee", "abc");
    UserModel userModel2 = new UserModel(2L, "User2", "employee", "abc1");
    UserModel userModel3 = new UserModel(3L, "User3", "employee", "abc2");
    UserModel userModel4 = new UserModel(4L, "User4", "employee", "abc3");

    List<Long> users1 = new ArrayList<>();
    List<Long> users2 = new ArrayList<>();
    List<GroupModel> groups = new ArrayList<>();
    GroupModel group1 = new GroupModel("group", userModel1, new ArrayList<>(), 2);
    GroupModel group2 = new GroupModel("group1", userModel2, new ArrayList<>(), 4);

    Group group = new Group("abc", user1, new ArrayList<>(), 3);

    @BeforeEach
    void setup() {
        users1.add(userModel2.getId());
        users1.add(userModel3.getId());
        users1.add(userModel4.getId());
        users2.add(userModel3.getId());
        group1.setGroupMembers(users1);
        group2.setGroupMembers(users2);
        groups.add(group1);
        groups.add(group2);
    }

    @Test
    void getGroupsOfUserNoGroups() {
        when(groupRepository.findAllByGroupIdExists()).thenReturn(Optional.of(new ArrayList<>()));
        assertThrows(NoAssignedGroupException.class, () -> groupService
                .getGroupsOfUser(userModel1));
    }

    @Test
    void getGroupsOfUserExistingGroups() throws NoAssignedGroupException {
        when(groupRepository.findAllByGroupIdExists()).thenReturn(Optional.of(groups));
        List<GroupModel> result = new ArrayList<>();
        result.add(group1);
        assertEquals(result, groupService.getGroupsOfUser(userModel4));
    }

    @Test
    void saveGroup() {
        groupService.saveGroup(group);
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void addGroupNoPreviousGroupId() throws InvalidGroupCredentialsException,
        ApiException {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(userModel1));
        GroupModel group = new GroupModel("group", userModel1, users1, 0);
        Group groupSaved = new Group(group.getName(), new User(userModel1.getNetId(),
                userModel1.getRole(), passwordEncoder.encode(userModel1.getPassword())),
                group.getGroupMembers(), group.getGroupId());
        assertEquals(group, groupService.addGroup("group", userModel1, users1));
        verify(groupRepository, times(1)).save(groupSaved);
    }

    @Test
    void verifyInvalidUsers() {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());
        assertThrows(InvalidGroupCredentialsException.class, () -> groupService
                .verifyUsers(users1));
    }
}