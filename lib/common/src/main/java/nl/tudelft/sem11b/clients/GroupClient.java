package nl.tudelft.sem11b.clients;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.services.GroupService;

public class GroupClient implements GroupService {

    private final ApiClient<Authenticated> api;

    /**
     * Instantiates the {@link UserClient} class.
     *
     * @param api API client with authentication
     */
    public GroupClient(ApiClient<Authenticated> api) {
        this.api = api;
    }

    @Override
    public List<GroupModel> getGroupsOfUser(UserModel user)
            throws NoAssignedGroupException, ApiException {
        return api.get("/groups?user=" + user,
                new TypeReference<List<GroupModel>>() {
                }).unwrap();
    }

    @Override
    public List<GroupModel> getGroupsOfSecretary(UserModel user, List<GroupModel> groups)
            throws ApiException {
        return api.get("/groups/secretary?user=" + user + "&groups=" + groups,
                new TypeReference<List<GroupModel>>() {
                }).unwrap();
    }

    @Override
    public GroupModel addGroup(String name, Long secretaryId, List<Long> groupMembers)
            throws InvalidGroupCredentialsException, InvalidCredentialsException, ApiException {
        GroupModel groupModel = new GroupModel(name, secretaryId, groupMembers);
        return api.get("/groups?model=" + groupModel,
                new TypeReference<GroupModel>() {
                }).unwrap();
    }

    @Override
    public GroupModel getGroupInfo(Long groupId)
            throws InvalidGroupCredentialsException, ApiException, InvalidCredentialsException {
        return api.get("/groups/" + groupId,
                new TypeReference<GroupModel>() {
                }).unwrap();
    }

    @Override
    public void addGroupMembers(List<Long> users, GroupModel group)
            throws InvalidGroupCredentialsException, ApiException {
        api.get("/groups/members?users=" + users + "&group=" + group,
                new TypeReference<GroupModel>() {
                }).unwrap();
    }
}
