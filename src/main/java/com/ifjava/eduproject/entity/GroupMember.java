package com.ifjava.eduproject.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_group_members_users_id"))
@Table(name = "group_members")
public abstract class GroupMember extends User {

    @ManyToMany(mappedBy = "groupMembers", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    public void addGroup(Group group) {
        this.groups.add(group);
        group.getGroupMembers().add(this);
    }

}
