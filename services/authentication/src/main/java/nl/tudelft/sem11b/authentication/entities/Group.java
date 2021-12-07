package nl.tudelft.sem11b.authentication.entities;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupId")
    private final int groupId;

    @Column(name = "secretary")
    private User secretary;

    @Column(name = "groupMembers")
    private List<User> groupMembers;

    /**
     * Constructor for Group class for generating a new group.
     *
     * @param secretary    of type User, who is the secretary of the group
     * @param groupMembers of type List, contains a list of users who are part of the group.
     * @param groupId      contains the groupId.
     */
    public Group(User secretary, List<User> groupMembers, int groupId) {
        this.secretary = secretary;
        this.groupMembers = groupMembers;
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public User getSecretary() {
        return secretary;
    }

    public List<User> getGroupMembers() {
        return groupMembers;
    }

    public void setSecretary(User secretary) {
        this.secretary = secretary;
    }

    public void setGroupMembers(List<User> groupMembers) {
        this.groupMembers = groupMembers;
    }

    /**
     * Adds a new user to the members of a specific group.
     *
     * @param newGroupMembers contains a list of new group members.
     * @return a new list of the current group members.
     */
    public List<User> addToGroupMembers(List<User> newGroupMembers) {
        for (User user : newGroupMembers) {
            if (!this.groupMembers.contains(user)) {
                this.groupMembers.add(user);
            }
        }
        return this.groupMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        return getGroupId() == group.getGroupId()
                && getSecretary().equals(group.getSecretary())
                && Objects.equals(getGroupMembers(), group.getGroupMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupId(), getSecretary(), getGroupMembers());
    }
}
