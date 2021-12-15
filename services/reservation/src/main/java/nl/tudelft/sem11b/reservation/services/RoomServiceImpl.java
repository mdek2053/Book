package nl.tudelft.sem11b.reservation.services;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import nl.tudelft.sem11b.clients.AuthenticatedServiceClient;
import nl.tudelft.sem11b.clients.RoomsClient;
import nl.tudelft.sem11b.data.exception.InvalidFilterException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.RoomModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;
import nl.tudelft.sem11b.services.RoomsService;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl extends AuthenticatedServiceClient<RoomsClient>
    implements RoomsService {
    public RoomServiceImpl() {
        super(URI.create("http://localhost:8081/"), "Room", RoomsClient::new);
    }

    @Override
    public PageData<RoomStudModel> listRooms(PageIndex page) throws ApiException {
        return openClient().listRooms(page);
    }

    @Override
    public PageData<RoomStudModel> listRooms(PageIndex page, long building)
        throws ApiException, EntityNotFound {
        return openClient().listRooms(page, building);
    }

    @Override
    public PageData<RoomStudModel> searchRooms(PageIndex page, Map<String, Object> filterValues)
            throws ApiException, EntityNotFound, InvalidFilterException {
        return null;
    }

    @Override
    public Optional<RoomModel> getRoom(long id) throws ApiException {
        return openClient().getRoom(id);
    }
}
