package com.softserve.itacademy.entity;


import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Material extends BasicEntity {

    private String name;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "file_reference")
    private String fileReference;
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_course_material"))
    private Course course;
}
