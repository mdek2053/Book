package nl.tudelft.sem11b.reservation.services;

import java.net.URI;
import java.util.List;

import nl.tudelft.sem11b.clients.AuthenticatedServiceClient;
import nl.tudelft.sem11b.clients.GroupClient;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.services.GroupService;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends AuthenticatedServiceClient<GroupService>
        implements GroupService {

    public GroupServiceImpl() {
        super(URI.create("http://localhost:8082/"), "Group", GroupClient::new);
    }

    @Override
    public List<GroupModel> getGroupsOfUser(Long id)
            throws ApiException {
        return openClient().getGroupsOfUser(id);
    }

    @Override
    public List<GroupModel> getGroupsOfSecretary(Long id)
            throws ApiException {
        return openClient().getGroupsOfSecretary(id);
    }

    @Override
    public List<GroupModel> getGroupsOfCurrentUser(Long id)
            throws InvalidData, ApiException {
        return openClient().getGroupsOfCurrentUser(id);
    }

    @Override
    public GroupModel addGroup(String name, Long secretaryId, List<Long> groupMembers)
            throws ApiException, InvalidData {
        return openClient().addGroup(name, secretaryId, groupMembers);
    }

    @Override
    public GroupModel getGroupInfo(Long groupId)
            throws ApiException, InvalidData {
        return openClient().getGroupInfo(groupId);
    }

    @Override
    public void addGroupMembers(List<Long> users, GroupModel group)
            throws ApiException, InvalidData {
        openClient().addGroupMembers(users, group);
    }
}
