package nl.tudelft.sem11b.services;

import java.util.List;

import nl.tudelft.sem11b.data.exception.InvalidCredentialsException;
import nl.tudelft.sem11b.data.exception.InvalidGroupCredentialsException;
import nl.tudelft.sem11b.data.exception.NoAssignedGroupException;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.data.models.UserModel;

public interface GroupService {

    List<GroupModel> getGroupsOfUser(UserModel user) throws NoAssignedGroupException;

    List<GroupModel> getGroupsOfSecretary(UserModel user, List<GroupModel> groups);

    GroupModel addGroup(String name, UserModel secretary, List<Long> groupMembers)
            throws InvalidGroupCredentialsException, InvalidCredentialsException, ApiException;

    void verifyUsers(List<Long> groupMembers) throws InvalidGroupCredentialsException;

    GroupModel getGroupInfo(int groupId) throws InvalidGroupCredentialsException;

    void addGroupMembers(List<Long> users, GroupModel group)
            throws InvalidGroupCredentialsException;
}
