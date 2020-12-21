package com.softserve.itacademy.entity;

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
@DiscriminatorValue("STUDENT")
@Entity
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_students_users_id"))
@Table(name = "students")
public class Student extends User {

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    public void addGroup(Group group) {
        group.getStudents().add(this);
        this.groups.add(group);
    }

}
