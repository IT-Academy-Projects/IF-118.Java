package com.softserve.itacademy.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("TEACHER")
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_teachers_users_id"))
@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<Group> groups;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<Course> courses;

}
