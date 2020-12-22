package com.softserve.itacademy.entity;

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
public class Group extends BasicEntity {
    private String name;

    private Integer ownerId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "groups_users",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__users__group_id"))},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__users__user_id"))}
    )
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "groups_courses",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__courses__group_id"))},
            inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__courses__groups__course_id"))}
    )
    private Set<Course> courses = new HashSet<>();

}
