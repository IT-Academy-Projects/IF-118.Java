package com.ifjava.eduproject.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @ManyToOne
    @JoinColumn(name = "course_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_groups_courses_group_id"))
    private Course course;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "groups_group_members",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_groups_group_members_groups"))},
            inverseJoinColumns = {@JoinColumn(name = "group_member_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_groups_group_members_group_members"))}
    )
    private Set<GroupMember> groupMembers = new HashSet<>();

}
