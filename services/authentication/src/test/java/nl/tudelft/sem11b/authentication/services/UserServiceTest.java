package nl.tudelft.sem11b.authentication.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.data.models.UserRequestModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private transient UserRepository userRepositoryMock;

    @Mock
    transient SecurityContext securityContext;

    @Mock
    transient Authentication authentication;

    @Mock
    private transient PasswordEncoder encoder;

    @InjectMocks
    private transient UserServiceImpl userService;

    private final transient String netId1 = "Andy";
    private final transient String netId2 = "Bob";

    transient String role = "employee";
    transient String plain = "plain";
    transient String encoded = "encoded";
    private final transient User plainTextPasswordUser = new User(netId2, role, plain);
    private final transient User encodedPasswordUser = new User(netId2, role, encoded);

    private final transient UserRequestModel
            plainTextPasswordUserModel = new UserRequestModel(netId2, plain, role);
    private final UserRequestModel encodedPasswordUserModel =
            new UserRequestModel("Bob", null, role);

    private final transient UserModel nullPasswordUserModel
            = new UserModel(1L, netId2, new String[0]);

    @Test
    public void loadUserNonExistentTest() {
        when(userRepositoryMock.findUserByNetId(netId1)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(netId1));
    }

    @Test
    public void loadUserSuccessfulTest() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(plainTextPasswordUser.getRole()));
        org.springframework.security.core.userdetails.User expected =
                new org.springframework.security.core.userdetails
                        .User(plainTextPasswordUser.getNetId(), plainTextPasswordUser
                        .getPassword(), authorities);

        when(userRepositoryMock.findUserByNetId(netId2))
                .thenReturn(Optional.of(plainTextPasswordUser));

        assertEquals(expected, userService.loadUserByUsername(netId2));
    }

    @Test
    public void getCurrentUserTest() throws InvalidCredentialsException, ApiException {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(netId2);
        when(userRepositoryMock.findUserByNetId(netId2))
                .thenReturn(Optional.of(plainTextPasswordUser));

        assertEquals(nullPasswordUserModel.getLogin(), userService.currentUser().getLogin());
    }

    @Test
    public void addUserInvalidNetIdTest() {
        assertThrows(InvalidData.class, () -> userService.addUser(
                null, "banana", null));
    }

    @Test
    public void addUserInvalidPasswordTest() {
        assertThrows(InvalidData.class, () -> userService.addUser(
                "Stefan", null, null));
    }

    @Test
    public void addUserAlreadyExistsTest() {
        when(userRepositoryMock.findUserByNetId(netId2))
                .thenReturn(Optional.of(plainTextPasswordUser));

        assertThrows(InvalidData.class, () ->
                userService.addUser(netId2, plain, null));
    }

    @Test
    public void addUserSuccessfulTest() throws InvalidData {
        when(userRepositoryMock.findUserByNetId(netId2)).thenReturn(Optional.empty());
        when(encoder.encode(plainTextPasswordUser.getPassword()))
                .thenReturn(encodedPasswordUser.getPassword());

        userService.addUser(netId2, plain, null);

        verify(userRepositoryMock, times(1)).save(encodedPasswordUser);
    }

}
