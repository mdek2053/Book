package nl.tudelft.sem11b.authentication.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import nl.tudelft.sem11b.data.models.GroupModel;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "groupId")
    private long groupId;

    @Column(name = "name")
    private String name;

    //@ManyToOne
    private Long secretary;

    @ElementCollection
    private List<Long> groupMembers;

    /**
     * Constructor for Group class for generating a new group.
     *
     * @param name         of type String, which is the name of the group.
     * @param secretary    of type User, who is the secretary of the group
     * @param groupMembers of type List, contains a list of users who are part of the group.
     * @param groupId      contains the groupId.
     */
    public Group(String name, Long secretary, List<Long> groupMembers, Long groupId) {
        this.name = name;
        this.secretary = secretary;
        this.groupMembers = groupMembers;
        this.groupId = groupId;
    }

    /**
     * Constructor specifically for adding new groups to the system.
     *
     * @param name          of type String, which is the name of the group.
     * @param secretary     of type User, who is the secretary of the group
     * @param groupMembers  of type List, contains a list of users who are part of the group.
     */
    public Group(String name, Long secretary, List<Long> groupMembers) {
        this.name = name;
        this.secretary = secretary;
        this.groupMembers = groupMembers;
    }

    public Group() {

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

    public void setSecretary(Long secretary) {
        this.secretary = secretary;
    }

    public void setGroupMembers(List<Long> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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

    public GroupModel createGroupModel() {
        return new GroupModel(this.name, this.secretary, this.groupMembers, this.groupId);
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
        return Objects.equals(getGroupId(), group.getGroupId())
                && Objects.equals(getName(), group.getName())
                && Objects.equals(getSecretary(), group.getSecretary())
                && Objects.equals(getGroupMembers(), group.getGroupMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupId(), getName(), getSecretary(), getGroupMembers());
    }
}
