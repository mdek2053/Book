package nl.tudelft.sem11b.services;

import java.util.List;

import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.GroupModel;

/**
 * API definition of the group service. This service is responsible for holding the information
 * about groups.
 */

public interface GroupService {

    List<GroupModel> getGroupsOfUser(Long id) throws NoAssignedGroupException, ApiException;

    List<GroupModel> getGroupsOfSecretary(Long id, List<GroupModel> groups)
            throws ApiException;

    GroupModel addGroup(String name, Long secretaryId, List<Long> groupMembers)
            throws InvalidGroupCredentialsException, InvalidCredentialsException, ApiException;

    GroupModel getGroupInfo(Long groupId) throws InvalidGroupCredentialsException,
            ApiException, InvalidCredentialsException;

    void addGroupMembers(List<Long> users, GroupModel group)
            throws InvalidGroupCredentialsException, ApiException;
}
