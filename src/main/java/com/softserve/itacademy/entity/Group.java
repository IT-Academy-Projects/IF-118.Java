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

    @ManyToOne
    @JoinColumn(name = "teacher_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__teachers__teacher_id"))
    private Teacher teacher;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "groups_students",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__students__group_id"))},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__students__student_id"))}
    )
    private Set<Student> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "groups_courses",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__groups__courses__group_id"))},
            inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__courses__groups__course_id"))}
    )
    private Set<Course> courses = new HashSet<>();

}
