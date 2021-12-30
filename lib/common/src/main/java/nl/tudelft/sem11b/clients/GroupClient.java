package nl.tudelft.sem11b.clients;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.http.ApiClient;
import nl.tudelft.sem11b.http.Authenticated;
import nl.tudelft.sem11b.services.GroupService;

public class GroupClient implements GroupService {

    private final transient ApiClient<Authenticated> api;

    /**
     * Instantiates the {@link UserClient} class.
     *
     * @param api API client with authentication
     */
    public GroupClient(ApiClient<Authenticated> api) {
        this.api = api;
    }

    @Override
    public List<GroupModel> getGroupsOfUser(Long id)
            throws ApiException {
        return api.get("/groups/user/" + id,
                new TypeReference<List<GroupModel>>() {
                }).unwrap();
    }

    @Override
    public List<GroupModel> getGroupsOfSecretary(Long id, List<GroupModel> groups)
            throws ApiException {
        return api.get("/groups/secretary/" + id,
                new TypeReference<List<GroupModel>>() {
                }).unwrap();
    }

    @Override
    public GroupModel addGroup(String name, Long secretaryId, List<Long> groupMembers)
            throws ApiException {
        GroupModel groupModel = new GroupModel(name, secretaryId, groupMembers);
        api.post("/groups/" + groupModel.getGroupId(), groupModel,
                new TypeReference<>() {
                }).unwrap();
        return groupModel;
    }

    @Override
    public GroupModel getGroupInfo(Long groupId)
            throws ApiException {
        return api.get("/groups/" + groupId,
                new TypeReference<GroupModel>() {
                }).unwrap();
    }

    @Override
    public void addGroupMembers(List<Long> users, GroupModel group)
            throws ApiException {
        api.get("/groups/members?users=" + users + "&group=" + group,
                new TypeReference<GroupModel>() {
                }).unwrap();
    }
}
