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
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class GroupServiceTest {

    transient GroupRepository groupRepository = mock(GroupRepository.class);

    transient UserRepository userRepository = mock(UserRepository.class);

    transient UserServiceImpl userService = mock(UserServiceImpl.class);

    transient PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @InjectMocks
    transient GroupServiceImpl groupService;

    transient String role = "employee";
    transient User user1 = new User(1L, "User", role, "abc");
    transient User user2 = new User(2L, "User2", role, "abc1");
    transient User user3 = new User(3L, "User3", role, "abc2");
    transient User user4 = new User(4L, "User4", role, "abc3");

    transient UserModel userModel1;
    transient UserModel userModel2;
    transient UserModel userModel3;
    transient UserModel userModel4;

    transient List<Long> users1 = new ArrayList<>();
    transient List<Long> users2 = new ArrayList<>();
    transient List<Group> groups = new ArrayList<>();
    transient Group group1 = new Group("group1", user1.getId(), new ArrayList<>(), 2L);
    transient Group group2 = new Group("group2", user2.getId(), new ArrayList<>(), 4L);
    transient Group group = new Group("group", user1.getId(), new ArrayList<>(), 3L);

    transient GroupModel groupModel1;
    transient GroupModel groupModel2;
    transient GroupModel groupModel;
    transient List<GroupModel> groupModels = new ArrayList<>();

    @BeforeEach
    void setup() {
        userModel1 = user1.toModel();
        userModel2 = user2.toModel();
        userModel3 = user3.toModel();
        userModel4 = user4.toModel();
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
    void addGroupNoPreviousGroupId() throws InvalidGroupCredentialsException,
        ApiException {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user1));

        when(groupRepository.save(Mockito.any(Group.class))).thenReturn(group);
        GroupModel expected = new GroupModel(group.getName(), group.getSecretary(),
                group.getGroupMembers(), group.getGroupId());
        assertEquals(expected, groupService.addGroup(group.getName(), userModel1.getId(),
                group.getGroupMembers()));
    }

    @Test
    void addGroupSecretaryIdNull() throws ApiException, InvalidGroupCredentialsException {
        when(userService.currentUser()).thenReturn(userModel1);
        when(userRepository.findUserByNetId(userModel1.getLogin())).thenReturn(Optional.of(user1));
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user1));

        when(groupRepository.save(Mockito.any(Group.class))).thenReturn(group1);
        GroupModel expected = new GroupModel(group1.getName(), group1.getSecretary(),
                group1.getGroupMembers(), group1.getGroupId());
        assertEquals(expected, groupService.addGroup(group.getName(), null, users1));
    }

    @Test
    void verifyInvalidUsers() {
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.empty());
        assertThrows(InvalidGroupCredentialsException.class, () -> groupService
                .verifyUsers(users1));
    }

    @Test
    void getInfoOfNonExistingGroup() {
        when(groupRepository.findGroupByGroupId(10L)).thenReturn(Optional.empty());
        assertThrows(InvalidGroupCredentialsException.class, () -> groupService.getGroupInfo(10L));
    }

    @Test
    void getGroupInfo() throws ApiException,
            InvalidCredentialsException, InvalidGroupCredentialsException {
        when(groupRepository.findGroupByGroupId(group1.getGroupId()))
                .thenReturn(Optional.of(group1));
        when(userService.currentUser()).thenReturn(userModel2);

        assertEquals(groupModel1, groupService.getGroupInfo(group1.getGroupId()));
    }

    @Test
    void getGroupsOfSecretary() {
        when(userRepository.findUserByNetId(user1.getNetId())).thenReturn(Optional.of(user1));
        when(groupRepository.findGroupsBySecretary(user1.getId())).thenReturn(Optional.of(groups));

        assertEquals(groupModels, groupService.getGroupsOfSecretary(userModel1, new ArrayList<>()));
    }
}