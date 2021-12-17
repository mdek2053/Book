package nl.tudelft.sem11b.admin.services;

import nl.tudelft.sem11b.admin.data.entities.Building;
import nl.tudelft.sem11b.admin.data.repositories.BuildingRepository;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuildingServiceImplTest {

    @Mock
    BuildingRepository buildings;

    @Mock
    UserService users;

    BuildingServiceImpl service;

    private final Building building1 =
            new Building(1, "idk", "drebbelweg", null, null, new HashSet<>());

    private final BuildingModel buildingModel1 = building1.toModel();

    private final String[] adminRoles = {"admin"};
    private final String[] employeeRoles = {"employee"};
    private final UserModel admin = new UserModel(1, "appel", adminRoles);
    private final UserModel employee = new UserModel(2, "banaan", employeeRoles);

    @BeforeEach
    void initService() {
        service = new BuildingServiceImpl(buildings, users);
    }

    @Test
    public void addBuildingUnauthorizedTest() throws ApiException {

        when(users.currentUser()).thenReturn(employee);

        assertThrows(ApiException.class, () -> {
            service.addBuilding(buildingModel1);
        });
    }

    @Test
    public void addBuildingAlreadyExistsTest() throws ApiException {

        when(users.currentUser()).thenReturn(admin);
        when(buildings.existsById(1L)).thenReturn(true);

        assertThrows(ApiException.class, () -> {
            service.addBuilding(buildingModel1);
        });
    }

    @Test
    public void addBuildingSuccessfulTest() throws ApiException {

        when(users.currentUser()).thenReturn(admin);
        when(buildings.existsById(1L)).thenReturn(false);

        service.addBuilding(buildingModel1);

        verify(buildings, times(1)).save(building1);
    }
}
