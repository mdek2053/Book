package nl.tudelft.sem11b.reservation.services;

import java.net.URI;
import java.util.List;

import nl.tudelft.sem11b.clients.AuthenticatedServiceClient;
import nl.tudelft.sem11b.clients.GroupClient;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
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
            throws NoAssignedGroupException, ApiException {
        return openClient().getGroupsOfUser(id);
    }

    @Override
    public List<GroupModel> getGroupsOfSecretary(Long id, List<GroupModel> groups)
            throws ApiException {
        return openClient().getGroupsOfSecretary(id, groups);
    }

    @Override
    public GroupModel addGroup(String name, Long secretaryId, List<Long> groupMembers)
            throws InvalidGroupCredentialsException, InvalidCredentialsException, ApiException {
        return openClient().addGroup(name, secretaryId, groupMembers);
    }

    @Override
    public GroupModel getGroupInfo(Long groupId)
            throws InvalidGroupCredentialsException, ApiException, InvalidCredentialsException {
        return openClient().getGroupInfo(groupId);
    }

    @Override
    public void addGroupMembers(List<Long> users, GroupModel group)
            throws InvalidGroupCredentialsException, ApiException {
        openClient().addGroupMembers(users, group);
    }
}
