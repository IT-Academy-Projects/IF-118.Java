package com.softserve.itacademy.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course extends BasicEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__courses__teachers__teacher_id"))
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Material> materials;
}
