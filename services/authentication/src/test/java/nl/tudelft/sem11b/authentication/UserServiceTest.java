package nl.tudelft.sem11b.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.authentication.repositories.UserRepository;
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
    private UserRepository userRepositoryMock;

    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private String netId1 = "Andy";
    private String netId2 = "Bob";

    private User plainTextPasswordUser = new User("Bob", "employee", "plain");
    private User encodedPasswordUser = new User("Bob", "employee", "encoded");

    @Test
    public void loadUserNonExistentTest() {
        when(userRepositoryMock.findUserByNetId(netId1)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(netId1);
        });
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
    public void getCurrentUserTest() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(netId2);
        when(userRepositoryMock.findUserByNetId(netId2))
                .thenReturn(Optional.of(plainTextPasswordUser));

        User expected = plainTextPasswordUser;
        assertEquals(expected, userService.getCurrentUser());
    }

    @Test
    public void addUserInvalidNetIdTest() {
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.addUser(new User(null, "employee", "banana"));
        });
    }

    @Test
    public void addUserInvalidRoleTest() {
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.addUser(new User("Stefan", null, "banana"));
        });
    }

    @Test
    public void addUserInvalidPasswordTest() {
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.addUser(new User("Stefan", "employee", null));
        });
    }

    @Test
    public void addUserAlreadyExistsTest() {
        when(userRepositoryMock.findUserByNetId(netId2))
                .thenReturn(Optional.of(plainTextPasswordUser));

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.addUser(plainTextPasswordUser);
        });
    }

    @Test
    public void addUserSuccessfulTest() {
        User expected = encodedPasswordUser;

        when(userRepositoryMock.findUserByNetId(netId2)).thenReturn(Optional.empty());
        when(encoder.encode(plainTextPasswordUser.getPassword()))
                .thenReturn(encodedPasswordUser.getPassword());

        try {
            assertEquals(expected, userService.addUser(plainTextPasswordUser));
        } catch (Exception e) {
            fail();
        }


        verify(userRepositoryMock, times(1)).save(encodedPasswordUser);
    }

}
