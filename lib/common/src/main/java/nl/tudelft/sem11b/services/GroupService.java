package nl.tudelft.sem11b.services;

import java.util.List;

import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.GroupModel;

/**
 * API definition of the group service. This service is responsible for holding the information
 * about groups.
 */

public interface GroupService {

    List<GroupModel> getGroupsOfUser(Long id) throws ApiException;

    List<GroupModel> getGroupsOfSecretary(Long id)
            throws ApiException;

    GroupModel addGroup(String name, Long secretaryId, List<Long> groupMembers)
            throws ApiException, InvalidData;

    GroupModel getGroupInfo(Long groupId) throws ApiException, InvalidData;

    void addGroupMembers(List<Long> users, Long groupId)
            throws ApiException, InvalidData;
}
