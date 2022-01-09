package nl.tudelft.sem11b.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.ClosureModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.ApiResponse;
import nl.tudelft.sem11b.http.Authenticated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoomsClientTest {

    @Mock
    ApiClient<Authenticated> api;

    RoomsClient client;

    RoomStudModel roomStudModel = new RoomStudModel(1L, "yeet", "chonk", 20);
    FaultStudModel faultStudModel = new FaultStudModel(1L, 1L, "is broken");
    FaultRequestModel faultRequestModel = new FaultRequestModel(1L, "Broken things");
    ClosureModel closure = new ClosureModel("the thing is broken");

    PageIndex pageIndex0 = new PageIndex(0, 10);
    PageIndex pageIndex1 = new PageIndex(1, 10);

    PageData<RoomStudModel> pageDataRoomStudModel1 = new PageData<>(1L, List.of(roomStudModel));
    PageData<FaultStudModel> pageDataFaultStudModel1 = new PageData<>(1L, List.of(faultStudModel));


    @BeforeEach
    void setup() {
        client = new RoomsClient(api);
    }

    @Test
    void listRoomsSpecificBuildingEmptyDataTest() {
        when(api.get(eq("/buildings/1/rooms?page=" + pageIndex0.getPage()
                + "&limit=" + pageIndex0.getLimit()), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms", null));

        assertThrows(EntityNotFound.class, () -> client.listRooms(pageIndex0, 1));
    }

    @Test
    void listRoomsSpecificBuildingTest() throws EntityNotFound, ApiException {
        when(api.get(eq("/buildings/1/rooms?page=" + pageIndex1.getPage()
                + "&limit=" + pageIndex1.getLimit()), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms", pageDataRoomStudModel1));

        assertEquals(pageDataRoomStudModel1, client.listRooms(pageIndex1, 1));
    }

    @Test
    void listFaultsSpecificRoomEmptyDataTest() {
        when(api.get(eq("/rooms/1/faults?page=" + pageIndex0.getPage()
                + "&limit=" + pageIndex0.getLimit()), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms", null));

        assertThrows(EntityNotFound.class, () -> client.listFaults(pageIndex0, 1));
    }

    @Test
    void listFaultsSpecificRoomTest() throws EntityNotFound, ApiException {
        when(api.get(eq("/rooms/1/faults?page=" + pageIndex1.getPage()
                + "&limit=" + pageIndex1.getLimit()), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms", pageDataFaultStudModel1));

        assertEquals(pageDataFaultStudModel1, client.listFaults(pageIndex1, 1));
    }

    @Test
    void listFaultsEmptyDataTest() {
        when(api.get(eq("/faults?page=" + pageIndex0.getPage() + "&limit="
                + pageIndex0.getLimit()), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms", null));

        assertThrows(EntityNotFound.class, () -> client.listFaults(pageIndex0));
    }

    @Test
    void listFaultsTest() throws EntityNotFound, ApiException {
        when(api.get(eq("/faults?page=" + pageIndex1.getPage() + "&limit="
                + pageIndex1.getLimit()), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms", pageDataFaultStudModel1));

        assertEquals(pageDataFaultStudModel1, client.listFaults(pageIndex1));
    }

    @Test
    void closeRoomUnsuccessfulTest() {
        when(api.post(eq("/rooms/1/closure"), eq(closure), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms", new ApiException("rooms", "Bam")));
        assertThrows(ApiException.class, () -> client.closeRoom(1, closure));
    }

    @Test
    void closeRoomSuccessfulTest() throws ApiException {
        when(api.post(eq("/rooms/1/closure"), eq(closure), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms"));
        client.closeRoom(1, closure);
    }

    @Test
    void addFaultUnsuccessfulTest() {
        when(api.post(eq("/rooms/1/fault"), eq(faultRequestModel), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms", new ApiException("rooms", "Bam")));
        assertThrows(ApiException.class, () -> client.addFault(1, faultRequestModel));
    }

    @Test
    void addFaultSuccessfulTest() throws ApiException {
        when(api.post(eq("/rooms/1/fault"), eq(faultRequestModel), any(TypeReference.class)))
                .thenReturn(new ApiResponse<>("rooms"));
        client.addFault(1, faultRequestModel);
    }

    @Test
    void reopenRoomUnsuccessfulTest() {
        when(api.delete(eq("/rooms/1/closure")))
                .thenReturn(new ApiResponse<>("rooms", new ApiException("rooms", "Bam")));
        assertThrows(ApiException.class, () -> client.reopenRoom(1));
    }

    @Test
    void reopenRoomSuccessfulTest() throws ApiException {
        when(api.delete(eq("/rooms/1/closure")))
                .thenReturn(new ApiResponse<>("rooms"));
        client.reopenRoom(1);
    }

    @Test
    void resolveFaultUnsuccessfulTest() {
        when(api.delete(eq("/faults/1")))
                .thenReturn(new ApiResponse<>("rooms", new ApiException("rooms", "Bam")));
        assertThrows(ApiException.class, () -> client.resolveFault(1));
    }

    @Test
    void resolveFaultSuccessfulTest() throws ApiException {
        when(api.delete(eq("/faults/1")))
                .thenReturn(new ApiResponse<>("rooms"));
        client.resolveFault(1);
    }

}
