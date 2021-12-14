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
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.GroupModel;
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

    UserModel userModel1 = new UserModel(1L, "User", new String[1]);
    UserModel userModel2 = new UserModel(2L, "User2", new String[1]);
    UserModel userModel3 = new UserModel(3L, "User3", new String[1]);
    UserModel userModel4 = new UserModel(4L, "User4", new String[1]);

    List<Long> users1 = new ArrayList<>();
    List<Long> users2 = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
    Group group1 = new Group("group", user1, new ArrayList<>(), 2L);
    Group group2 = new Group("group1", user2, new ArrayList<>(), 4L);
    Group group = new Group("abc", user1, new ArrayList<>(), 3L);

    GroupModel groupModel1;
    GroupModel groupModel2;
    GroupModel groupModel;
    List<GroupModel> groupModels = new ArrayList<>();

    @BeforeEach
    void setup() {
        users1.add(userModel2.getId());
        users1.add(userModel3.getId());
        users1.add(userModel4.getId());
        users2.add(userModel3.getId());
        group1.setGroupMembers(users1);
        group2.setGroupMembers(users2);
        groups.add(group1);

        groupModel1 = group1.createGroupModel();
        groupModel = group.createGroupModel();
        groupModel2 = group2.createGroupModel();
        groupModels.add(groupModel1);
    }

    @Test
    void getGroupsOfUserNoGroups() {
        when(groupRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoAssignedGroupException.class, () -> groupService
                .getGroupsOfUser(userModel1));
    }

    @Test
    void getGroupsOfUserExistingGroups() throws NoAssignedGroupException {
        when(groupRepository.findAll()).thenReturn(groups);
        List<GroupModel> result = new ArrayList<>();
        result.add(groupModel1);
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
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user1));
        GroupModel group = new GroupModel("name", userModel1.getId(), users1);
        Group groupSaved = new Group(group.getName(), new User(user1.getNetId(),
                user1.getRole(), passwordEncoder.encode(user1.getPassword())),
                group.getGroupMembers(), group.getGroupId());
        assertEquals(group, groupService.addGroup(group.getName(), userModel1.getId(), users1));
        verify(groupRepository, times(1)).save(groupSaved);
    }

    @Test
    void verifyInvalidUsers() {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());
        assertThrows(InvalidGroupCredentialsException.class, () -> groupService
                .verifyUsers(users1));
    }
}