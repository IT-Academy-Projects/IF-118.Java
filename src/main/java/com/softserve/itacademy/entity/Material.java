package com.softserve.itacademy.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Material extends BasicEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_course_material"))
    private Course course;
}
