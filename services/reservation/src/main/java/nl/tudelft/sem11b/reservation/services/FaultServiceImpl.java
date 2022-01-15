package nl.tudelft.sem11b.reservation.services;

import java.net.URI;
import java.util.Optional;

import nl.tudelft.sem11b.clients.AuthenticatedServiceClient;
import nl.tudelft.sem11b.clients.FaultsClient;
import nl.tudelft.sem11b.clients.RoomsClient;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.services.FaultService;

public class FaultServiceImpl extends AuthenticatedServiceClient<FaultsClient>
    implements FaultService {
    public FaultServiceImpl() {
        super(URI.create("http://localhost:8081/"), "Room", FaultsClient::new);
    }

    @Override
    public void addFault(long roomId, FaultRequestModel faultRequest) throws ApiException {
        openClient().addFault(roomId, faultRequest);
    }

    @Override
    public PageData<FaultStudModel> listFaults(PageIndex page, long roomId)
        throws ApiException, EntityNotFound {
        return openClient().listFaults(page, roomId);
    }

    @Override
    public PageData<FaultModel> listFaults(PageIndex page) throws EntityNotFound, ApiException {
        return openClient().listFaults(page);
    }

    @Override
    public Optional<FaultModel> getFault(long id) throws ApiException {
        return openClient().getFault(id);
    }

    @Override
    public void resolveFault(long id) throws ApiException {
        openClient().resolveFault(id);
    }
}
