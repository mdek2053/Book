package nl.tudelft.sem11b.authentication.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.Group;
import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.exceptions.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.authentication.exceptions.NoAssignedGroupException;
import nl.tudelft.sem11b.authentication.repositories.GroupRepository;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class GroupServiceTest {

    @Mock
    GroupRepository groupRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @InjectMocks
    GroupService groupService;

    User user1 = new User("User", "employee", "abc");
    User user2 = new User("User2", "employee", "abc1");
    User user3 = new User("User3", "employee", "abc2");
    User user4 = new User("User4", "employee", "abc3");
    List<User> users1 = new ArrayList<>();
    List<User> users2 = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
    Group group1 = new Group(user1, new ArrayList<>(), 2);
    Group group2 = new Group(user2, new ArrayList<>(), 4);

    private final User plainTextPasswordUser = new User("Bob", "employee", "plain");

    @BeforeEach
    void setup() {
        users1.add(user2);
        users1.add(user3);
        users1.add(user4);
        users2.add(user3);
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

    @Test
    void getGroupsOfUserExistingGroups() throws NoAssignedGroupException {
        when(groupRepository.findAllByGroupIdExists()).thenReturn(Optional.of(groups));
        List<Group> result = new ArrayList<>();
        result.add(group1);
        assertEquals(result, groupService.getGroupsOfUser(user2));
    }

    @Test
    void getGroupsOfSecretaryNoGroups() {
        when(groupRepository.findGroupsBySecretary(any())).thenReturn(Optional.empty());
        assertThrows(NoAssignedGroupException.class, () -> groupService
                .getGroupsOfSecretary(user1));
    }

    @Test
    void getGroupsOfSecretaryExistingGroups() throws NoAssignedGroupException {
        when(groupRepository.findGroupsBySecretary(any())).thenReturn(Optional.of(groups));
        assertEquals(groups, groupService.getGroupsOfSecretary(user2));
    }

    @Test
    void saveGroup() {
        groupService.saveGroup(group1);
        verify(groupRepository, times(1)).save(group1);
    }

    @Test
    void addGroupNoSecretary() {
        assertThrows(InvalidGroupCredentialsException.class, () -> groupService
                .addGroup(null, users1));
    }

    //@Test
    void addGroupNoPreviousGroupId() throws InvalidGroupCredentialsException {
        when(userRepository.findUserByNetId(anyString())).thenReturn(Optional.of(user1));
        when(groupRepository.findTopByOrderByGroupIdDesc()).thenReturn(null);
        Group group = new Group(user1, users1, 1);
        assertEquals(group, groupService.addGroup(user1, users1));
        verify(groupRepository, times(1)).save(group);
    }

    //@Test
    void addGroupWithPreviousGroupId() throws InvalidGroupCredentialsException {
        when(userService.loadUserByUsername(user3.getNetId()))
                .thenReturn(new org.springframework.security.core.userdetails
                        .User(user3.getNetId(), user3.getPassword(), new ArrayList<>()));

        when(userRepository.findUserByNetId(anyString()))
                .thenReturn(Optional.of(user3));


        when(groupRepository.findTopByOrderByGroupIdDesc()).thenReturn(Optional.of(2));
        Group group = new Group(user1, users1, 3);
        assertEquals(group, groupService.addGroup(user1, users2));
        verify(groupRepository, times(1)).save(group);
    }

    //@Test
    void verifyInvalidUsers() {
        //when(userService.loadUserByUsername(anyString())).thenReturn();
        when(userRepository.findUserByNetId(anyString())).thenReturn(Optional.empty());
        assertThrows(InvalidGroupCredentialsException.class, () -> groupService
                .verifyUsers(users1));
    }
}