package nl.tudelft.sem11b.data.models;

import java.util.List;
import java.util.Objects;

public class GroupModel {

    private Long groupId;
    private String name;
    private Long secretary;
    private List<Long> groupMembers;

    /**
     * Constructor for Group class for generating a new group.
     *
     * @param name         of type String, which is the name of the group.
     * @param secretary    of type UserModel, who is the secretary of the group
     * @param groupMembers of type List, contains a list of users who are part of the group.
     * @param groupId      contains the groupId.
     */
    public GroupModel(String name, Long secretary, List<Long> groupMembers, Long groupId) {
        this.name = name;
        this.secretary = secretary;
        this.groupMembers = groupMembers;
        this.groupId = groupId;
    }

    public GroupModel() {

    }

    /**
     * Constructor specifically for adding new groups to the system.
     *
     * @param name          of type String, which is the name of the group.
     * @param secretary     of type UserModel, who is the secretary of the group
     * @param groupMembers  of type List, contains a list of users who are part of the group.
     */
    public GroupModel(String name, Long secretary, List<Long> groupMembers) {
        this.name = name;
        this.secretary = secretary;
        this.groupMembers = groupMembers;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getSecretary() {
        return secretary;
    }

    public List<Long> getGroupMembers() {
        return groupMembers;
    }

    public String getName() {
        return name;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setSecretary(Long secretary) {
        this.secretary = secretary;
    }

    public void setGroupMembers(List<Long> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a new user to the members of a specific group.
     *
     * @param newGroupMembers contains a list of new group members.
     */
    public void addToGroupMembers(List<Long> newGroupMembers) {
        for (Long id : newGroupMembers) {
            if (!this.groupMembers.contains(id)) {
                this.groupMembers.add(id);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupModel that = (GroupModel) o;
        return getGroupId().equals(that.getGroupId())
                && Objects.equals(getName(), that.getName())
                && getSecretary().equals(that.getSecretary())
                && Objects.equals(getGroupMembers(), that.getGroupMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupId(), getName(), getSecretary(), getGroupMembers());
    }

    @Override
    public String toString() {
        return "GroupModel{"
                + "groupId=" + groupId
                + ", name='" + name + '\''
                + ", secretary=" + secretary
                + ", groupMembers=" + groupMembers
                + '}';
    }
}
